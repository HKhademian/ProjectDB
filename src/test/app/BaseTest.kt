package app

import app.repository.DB_NAME
import app.repository.TEST_DB_NAME
import org.junit.*
import org.junit.rules.TestName
import java.io.File


open class BaseTest {
	companion object {
		@BeforeClass
		@JvmStatic
		fun `init test db`() {
			File(TEST_DB_NAME).delete()
			File(DB_NAME).copyTo(File(TEST_DB_NAME), overwrite = true)
			Thread.sleep(100);
			app.repository.test = true
		}

		@AfterClass
		@JvmStatic
		fun `delete test db`() {
			File(TEST_DB_NAME).delete()
		}
	}

	@Rule
	@JvmField
	var testName = TestName()

	@Before
	fun `before test`() {
		println("---\n--- ${testName.methodName} ---\n---")
	}

	@After
	fun `after test`() {
		println("---- ----------------------- ---")
	}
}
