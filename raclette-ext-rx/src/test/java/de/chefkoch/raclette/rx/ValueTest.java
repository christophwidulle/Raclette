package de.chefkoch.raclette.rx;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.junit.Assert.*;

/**
 * Created by christophwidulle on 07.10.16.
 */

//@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
//@RunWith(RobolectricGradleTestRunner.class)
public class ValueTest {

    @Test
    public void testCreate() throws Exception {

        final Value<String> value = Value.create("1");
        assertEquals(value.get(), "1");
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        Subscription subscribe = value.asObservable().subscribe(testSubscriber);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue("1");

        value.set("2");

        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues("1", "2");

        subscribe.unsubscribe();

        testSubscriber = new TestSubscriber<>();
        value.asObservable().subscribe(testSubscriber);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue("2");

    }


    @Test
    public void testCreatePublish() throws Exception {

        final Value<String> value = Value.createPublish("1");
        assertEquals(value.get(), "1");
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        Subscription subscribe = value.asObservable().subscribe(testSubscriber);

        testSubscriber.assertValueCount(0);

        value.set("2");

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValues("2");

        subscribe.unsubscribe();

        testSubscriber = new TestSubscriber<>();
        value.asObservable().subscribe(testSubscriber);

        testSubscriber.assertValueCount(0);
    }


    @Test
    public void testCreateReplay() throws Exception {

        final Value<String> value = Value.createReplay("1");
        assertEquals(value.get(), "1");
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        Subscription subscribe = value.asObservable().subscribe(testSubscriber);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue("1");

        value.set("2");

        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues("1", "2");

        subscribe.unsubscribe();

        testSubscriber = new TestSubscriber<>();
        value.asObservable().subscribe(testSubscriber);

        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues("1", "2");
    }


    @Test
    public void testAsSetAction() throws Exception {
        final Value<String> value = Value.create("1");
        assertEquals(value.get(), "1");
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        Subscription subscribe = value.asObservable().subscribe(testSubscriber);

        value.asSetAction().call("2");

        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues("1", "2");

    }
}