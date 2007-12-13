/* 
 * Copyright 2007 Luke Daley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gldapo.schema.injecto
import gldapo.test.GldapoMockOperationInstaller as OI
import injecto.*


/**
 * @todo These tests need to be more comprehensive
 */
class SearchingInjectoTest extends GroovyTestCase 
{
	SearchingInjectoTest()
	{
		use (Injecto) { SearchingInjectoTestSchema.inject(SearchingInjecto) }
	}
	
	void testFindAll() 
	{
		OI.installSearchWithResult([1,2,3])
		assertEquals([1,2,3], SearchingInjectoTestSchema.findAll())
	}
	
	void testFind()
	{
		OI.installSearchWithResult([1,2,3])
		assertEquals(1, SearchingInjectoTestSchema.find())
		
		OI.installSearchWithResult([])
		assertEquals(null, SearchingInjectoTestSchema.find())
	}
}

class SearchingInjectoTestSchema {}