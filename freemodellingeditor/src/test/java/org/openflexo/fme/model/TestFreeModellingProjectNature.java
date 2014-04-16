package org.openflexo.fme.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * This unit test is intented to test project creation in the context of FreeModellingProjectNature
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestFreeModellingProjectNature extends OpenflexoProjectAtRunTimeTestCase {

	@Test
	@TestOrder(1)
	public void testRetrieveNatureFromClass() {

		instanciateTestServiceManager();

		FreeModellingProjectNature FREE_MODELLING_NATURE = serviceManager.getProjectNatureService().getProjectNature(
				FreeModellingProjectNature.class);
		assertNotNull(FREE_MODELLING_NATURE);

	}

	@Test
	@TestOrder(2)
	public void testRetrieveNatureFromClassName() {

		FreeModellingProjectNature FREE_MODELLING_NATURE = (FreeModellingProjectNature) serviceManager.getProjectNatureService()
				.getProjectNature("org.openflexo.fme.model.FreeModellingProjectNature");
		assertNotNull(FREE_MODELLING_NATURE);

	}

}
