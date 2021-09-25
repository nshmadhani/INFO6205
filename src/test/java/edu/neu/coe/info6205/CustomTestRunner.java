package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.TimerTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class CustomTestRunner {

        public static void main(String[] args) {
            Result result = JUnitCore.runClasses(TimerTest.class);

            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }

            System.out.println(result.wasSuccessful());
        }

}
