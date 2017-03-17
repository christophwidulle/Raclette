package de.chefkoch.raclette;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by christophwidulle on 17.03.17.
 */
public class ViewModelManagerTest {


    private final ViewModelIdGenerator idGenerator = new ViewModelIdGenerator() {
        @Override
        public String createId() {
            return "testid";
        }
    };

    @org.junit.Test
    public void createViewModel() throws Exception {

        ViewModelManager viewModelManager = new ViewModelManager(null, null, idGenerator);
        TestViewModel viewModel = viewModelManager.createViewModel(TestViewModel.class);

        assertNotNull(viewModel);
        assertThat(viewModel.getId(), equalTo("testid"));
        assertThat(viewModel.getState(), equalTo(ViewModelLifecycleState.NEW));
        assertThat(viewModel.getTestValue(), nullValue());

        assertTrue(viewModelManager.getViewModel("testid") == viewModel);
    }

    @org.junit.Test
    public void getViewModel() throws Exception {

        ViewModelManager viewModelManager = new ViewModelManager(null, null, idGenerator);
        TestViewModel viewModel = viewModelManager.createViewModel(TestViewModel.class);

        assertNotNull(viewModel);
        assertThat(viewModel.getId(), equalTo("testid"));
        assertThat(viewModel.getState(), equalTo(ViewModelLifecycleState.NEW));
        assertThat(viewModel.getTestValue(), nullValue());

        assertTrue(viewModelManager.getViewModel("testid") == viewModel);
    }


    @org.junit.Test
    public void createViewModelWithInjector() throws Exception {

        ViewModelInjector viewModelInjector = new ViewModelInjector() {
            @Override
            public void inject(ViewModel viewModel) {
                if (viewModel instanceof TestViewModel) {
                    ((TestViewModel) viewModel).setTestValue("testval");
                }
            }
        };

        ViewModelManager viewModelManager = new ViewModelManager(viewModelInjector, null, idGenerator);
        TestViewModel viewModel = viewModelManager.createViewModel(TestViewModel.class);

        assertThat(viewModel.getTestValue(), equalTo("testval"));
    }

    @org.junit.Test
    public void createViewModelWithFactory() throws Exception {


        ViewModelFactory viewModelfactory = new ViewModelFactory() {
            @Override
            public ViewModel create(Class viewModelType) {
                if (viewModelType == TestViewModel.class) {
                    TestViewModel testViewModel = new TestViewModel();
                    testViewModel.setTestValue("testval");
                    return testViewModel;
                }
                return null;
            }
        };

        ViewModelManager viewModelManager = new ViewModelManager(null, viewModelfactory, idGenerator);
        TestViewModel viewModel = viewModelManager.createViewModel(TestViewModel.class);

        assertThat(viewModel.getTestValue(), equalTo("testval"));

    }

    @org.junit.Test
    public void createViewModelWithFactoryAndInjector() throws Exception {


        ViewModelInjector viewModelInjector = new ViewModelInjector() {
            @Override
            public void inject(ViewModel viewModel) {
                if (viewModel instanceof TestViewModel) {
                    ((TestViewModel) viewModel).setTestValue("testval");
                }
            }
        };

        ViewModelFactory viewModelfactory = new ViewModelFactory() {
            @Override
            public ViewModel create(Class viewModelType) {
                if (viewModelType == TestViewModel.class) {
                    return new TestViewModel();
                }
                return null;
            }
        };

        ViewModelManager viewModelManager = new ViewModelManager(viewModelInjector, viewModelfactory, idGenerator);
        TestViewModel viewModel = viewModelManager.createViewModel(TestViewModel.class);

        assertThat(viewModel.getTestValue(), equalTo("testval"));

    }

    @org.junit.Test
    public void delete() throws Exception {
        ViewModelManager viewModelManager = new ViewModelManager(null, null, idGenerator);
        viewModelManager.delete("testid");
        assertNull(viewModelManager.getViewModel("testid"));
    }


    @org.junit.Test
    public void getAll() throws Exception {
        ViewModelManager viewModelManager = new ViewModelManager(null, null, new ViewModelIdGenerator.Default());
        viewModelManager.createViewModel(TestViewModel.class);
        viewModelManager.createViewModel(TestViewModel.class);

        assertThat(viewModelManager.getAll().size(), equalTo(2));

    }


    public static class TestViewModel extends ViewModel {

        private String testValue;

        public TestViewModel() {
        }

        public String getTestValue() {
            return testValue;
        }

        public void setTestValue(String testValue) {
            this.testValue = testValue;
        }
    }

}