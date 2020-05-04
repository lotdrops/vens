package hackovid.vens.common.data.updatedata

import hackovid.vens.common.data.LocalStorageMock
import hackovid.vens.common.data.RemoteConfigMock
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.data.TestCoroutineRule
import hackovid.vens.common.data.core.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import java.util.concurrent.TimeUnit
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UpdateDataUseCaseTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private val updateStoresDataSource = UpdateStoresDataSourceMock()
    private val localStorage = LocalStorageMock()
    private val remoteConfig = RemoteConfigMock()

    @MockK
    lateinit var remoteDataSource: UpdateDataDataSource

    @MockK
    lateinit var mockLocalDataSource: UpdateStoresDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coEvery { mockLocalDataSource.deleteStore(any()) } just runs
        coEvery { mockLocalDataSource.upsertStore(any()) } just runs
        localStorage.setLastUpdateTimestamp(INITIAL_UPDATE_TIMESTAMP)
    }

    @Test
    fun `updateData does not write to data source or local storage when remote returns an error`() =
        coroutineRule.runBlocking {
            val useCase = buildUseCaseForVerify()
            coEvery { remoteDataSource.getNewData(any()) } returns Resource.Failure(Throwable())

            useCase.updateData()

            coVerify(exactly = 0) { mockLocalDataSource.deleteStore(any()) }
            coVerify(exactly = 0) { mockLocalDataSource.upsertStore(any()) }
        }

    @Test
    fun `updateData does not update timestamp when remote returns an error`() =
        coroutineRule.runBlocking {
            val useCase = buildUseCaseForVerify()
            coEvery { remoteDataSource.getNewData(any()) } returns
                    Resource.Failure(Throwable())

            useCase.updateData()

            assertEquals(INITIAL_UPDATE_TIMESTAMP, localStorage.getLastUpdateTimestamp())
        }

    @Test
    fun `updateData does not write to data source when store has been added and removed`() =
        coroutineRule.runBlocking {
            val useCase = buildUseCaseForVerify()
            coEvery { remoteDataSource.getNewData(any()) } returns
                    Resource.Success(storeAddedAndRemoved())

            useCase.updateData()

            coVerify(exactly = 0) { mockLocalDataSource.deleteStore(any()) }
            coVerify(exactly = 0) { mockLocalDataSource.upsertStore(any()) }
        }

    @Test
    fun `updateData updates timestamp when store has been added and removed`() =
        coroutineRule.runBlocking {
            val useCase = buildUseCaseForVerify()
            val updates = storeAddedAndRemoved()
            coEvery { remoteDataSource.getNewData(any()) } returns
                    Resource.Success(updates)
            val expectedTimestamp = updates.maxBy { it.lastModified }!!.lastModified

            useCase.updateData()

            assertEquals(expectedTimestamp, localStorage.getLastUpdateTimestamp())
        }

    @Test
    fun `updateData writes only once to data source when a store has been updated and removed`() =
        coroutineRule.runBlocking {
            val useCase = buildUseCaseForVerify()
            coEvery { remoteDataSource.getNewData(any()) } returns
                    Resource.Success(storeUpdatedAndRemoved())

            useCase.updateData()

            coVerify(exactly = 1) { mockLocalDataSource.deleteStore(STORE_ID) }
            coVerify(exactly = 0) { mockLocalDataSource.upsertStore(any()) }
        }

    @Test
    fun `updateData writes only once latest data when store has been added and updated several times`() =
        coroutineRule.runBlocking {
            val useCase = buildUseCaseForVerify()
            coEvery { remoteDataSource.getNewData(any()) } returns
                    Resource.Success(storeAddedAndUpdated())
            val expectedStore = buildCompleteStore(STORE_ID)

            useCase.updateData()

            coVerify(exactly = 0) { mockLocalDataSource.deleteStore(any()) }
            coVerify(exactly = 1) { mockLocalDataSource.upsertStore(expectedStore) }
        }

    @Test
    fun `updateData updates data correctly when several changes are made`() =
        coroutineRule.runBlocking {
            val initialList = listOf(
                buildStore(id = 0),
                buildStore(id = 1),
                buildStore(id = 2)
            )
            val storeChanges = listOf(
                storeAddedAndRemoved(TIME_MODIFIED, 3),
                storeUpdated(TIME_MODIFIED + 1, 1),
                storeUpdatedAndRemoved(TIME_MODIFIED + 2, 2),
                storeAddedAndUpdated(TIME_MODIFIED + 3, 4),
                storeAdded(TIME_MODIFIED + 4, 5)
            ).flatten()
            val finalList = listOf(
                buildStore(id = 0),
                buildUpdatedStore(id = 1),
                buildCompleteStore(id = 4),
                buildStore(id = 5)
            )
            updateStoresDataSource.stores.clear()
            updateStoresDataSource.stores.addAll(initialList)
            val useCase = buildUseCaseWithInteraction()
            coEvery { remoteDataSource.getNewData(any()) } returns
                    Resource.Success(storeChanges)

            useCase.updateData()
            val result = updateStoresDataSource.stores
            result.sortBy { it.id }

            assertEquals(finalList, result)
        }

    @Test
    fun `updateData updates timestamp correctly when several changes are made`() =
        coroutineRule.runBlocking {
            val initialList = listOf(
                buildStore(id = 0),
                buildStore(id = 1),
                buildStore(id = 2)
            )
            val storeChanges = listOf(
                storeAddedAndRemoved(TIME_MODIFIED, 3),
                storeUpdated(TIME_MODIFIED + 1, 1),
                storeUpdatedAndRemoved(TIME_MODIFIED + 2, 2),
                storeAddedAndUpdated(TIME_MODIFIED + 3, 4)
            ).flatten()

            updateStoresDataSource.stores.clear()
            updateStoresDataSource.stores.addAll(initialList)
            val useCase = buildUseCaseWithInteraction()
            coEvery { remoteDataSource.getNewData(any()) } returns
                    Resource.Success(storeChanges)
            val expectedTimestamp = storeChanges.maxBy { it.lastModified }!!.lastModified

            useCase.updateData()

            assertEquals(expectedTimestamp, localStorage.getLastUpdateTimestamp())
        }

    @Test
    fun `updateData does not fetch data when last fetch is more recent than minimum time specified`() =
        coroutineRule.runBlocking {
            localStorage.setLastUpdateTimestamp(System.currentTimeMillis())
            val useCase = buildUseCaseForVerify()

            useCase.updateData()

            coVerify(exactly = 0) { remoteDataSource.getNewData(any()) }
        }

    @Test
    fun `updateData fetches data with saved timestamp when last fetch is older the configured interval`() =
        coroutineRule.runBlocking {
            val configInterval = TimeUnit.HOURS.toMillis(remoteConfig.hoursIntervalRefreshStores)
            localStorage.setLastUpdateTimestamp(System.currentTimeMillis() - configInterval)
            val useCase = buildUseCaseForVerify()
            coEvery { remoteDataSource.getNewData(any()) } returns Resource.Failure(Throwable())

            useCase.updateData()
            val expectedFetchTimestamp = localStorage.getLastUpdateTimestamp()

            coVerify(exactly = 1) { remoteDataSource.getNewData(expectedFetchTimestamp) }
        }

    private fun buildUseCaseForVerify() =
        UpdateDataUseCase(remoteDataSource, mockLocalDataSource, localStorage, remoteConfig)

    private fun buildUseCaseWithInteraction() =
        UpdateDataUseCase(remoteDataSource, updateStoresDataSource, localStorage, remoteConfig)

    private fun storeAddedAndRemoved(time: Long = TIME_MODIFIED, id: Long = STORE_ID) = listOf(
        StoreUpdateModel.Added(time, buildStore(id)),
        StoreUpdateModel.Updated(time, buildStore(id).copy(name = "New name")),
        StoreUpdateModel.Removed(id, time)
    )

    private fun storeUpdatedAndRemoved(time: Long = TIME_MODIFIED, id: Long = STORE_ID) = listOf(
        StoreUpdateModel.Updated(time, buildStore(id).copy(name = "New name")),
        StoreUpdateModel.Updated(time, buildUpdatedStore(id)),
        StoreUpdateModel.Removed(id, time)
    )

    private fun storeAddedAndUpdated(time: Long = TIME_MODIFIED, id: Long = STORE_ID) = listOf(
        StoreUpdateModel.Added(time, buildStore(id)),
        StoreUpdateModel.Updated(time + 10, buildStore(id).copy(name = "New name")),
        StoreUpdateModel.Updated(time + 20, buildCompleteStore(id))
    )

    private fun storeUpdated(time: Long = TIME_MODIFIED, id: Long = STORE_ID) = listOf(
        StoreUpdateModel.Updated(time, buildStore(id).copy(name = "New name")),
        StoreUpdateModel.Updated(time + 10, buildUpdatedStore(id))
    )

    private fun storeAdded(time: Long = TIME_MODIFIED, id: Long = STORE_ID) = listOf(
        StoreUpdateModel.Added(time, buildStore(id))
    )

    private fun buildStore(id: Long = STORE_ID) = Store(
        id = id,
        latitude = 0.0,
        longitude = 0.0,
        name = "",
        type = StoreType.EVERYDAY,
        subtype = StoreSubtype.SELF_SERVICE_SUPERMARKET
    )

    private fun buildUpdatedStore(id: Long = STORE_ID) = buildStore(id).copy(name = "New name 2")

    private fun buildCompleteStore(id: Long = STORE_ID) = Store(
        id = id,
        latitude = 2.0,
        longitude = 3.1,
        name = "Name of store",
        type = StoreType.FASHION,
        subtype = StoreSubtype.BAZAAR,
        phone = "123456789",
        mobilePhone = "612345789",
        address = "Shop Street, 13",
        web = "www.myShop.com",
        email = "name@myShop.com",
        schedule = "9:00 to 17:00",
        acceptsOrders = true,
        delivers = false
    )
}
private const val STORE_ID = 1L
private const val TIME_MODIFIED = 1L
private const val INITIAL_UPDATE_TIMESTAMP = 1L
