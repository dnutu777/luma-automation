package com.luma.core.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentReportsListener implements ITestListener {
    private static final String REPORT_FILE_PATH = "target/test-output/ExtentReport.html";
    private ExtentReports extentReports;
    private Map<String, ThreadLocal<ExtentTest>> testMap = new ConcurrentHashMap<>();
    private Screenshot screenshot = new Screenshot();

    @Override
    public void onStart(ITestContext context) {
        synchronized (this) {
            ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(REPORT_FILE_PATH);
            configureReporter(extentSparkReporter);

            extentReports = new ExtentReports();
            extentReports.attachReporter(extentSparkReporter);
            extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
            extentReports.setSystemInfo("Environment", System.getProperty("env"));
            extentReports.setSystemInfo("Browser", System.getProperty("browser"));
            extentReports.setSystemInfo("Headless", System.getProperty("headless"));
            extentReports.setSystemInfo("Grid", System.getProperty("useGrid"));
        }
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        String methodName = iTestResult.getMethod().getRealClass().getSimpleName() + "_" + getTestName(iTestResult);
        ExtentTest extentTest = extentReports.createTest(methodName, iTestResult.getMethod().getDescription());

        for (String group : iTestResult.getMethod().getGroups()) {
            extentTest.assignCategory(group);
        }

        synchronized(this) {
            ThreadLocal<ExtentTest> extentTestThreadLocal = testMap.getOrDefault(methodName, new ThreadLocal<>());
            extentTestThreadLocal.set(extentTest);
            testMap.put(methodName, extentTestThreadLocal);
        }
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        String methodName = iTestResult.getMethod().getRealClass().getSimpleName() + "_" + getTestName(iTestResult);
        ExtentTest extentTest = getExtentTest(methodName);
        extentTest.log(Status.PASS, methodName + " passed successfully.");

        logTestParameters(iTestResult, extentTest);
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        String methodName = iTestResult.getMethod().getRealClass().getSimpleName() + "_" + getTestName(iTestResult);
        ExtentTest extentTest = getExtentTest(methodName);

        screenshot.logFailureScreenshot(iTestResult, extentTest);
        extentTest.fail(iTestResult.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        String methodName = iTestResult.getMethod().getRealClass().getSimpleName() + "_" + getTestName(iTestResult);
        ExtentTest extentTest = getExtentTest(methodName);
        extentTest.log(Status.SKIP, methodName + " skipped.");

        if (iTestResult.getThrowable() != null) {
            extentTest.log(Status.INFO, "Reason for skipping: " + iTestResult.getThrowable().getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extentReports.flush();
    }

    private void logTestParameters(ITestResult iTestResult, ExtentTest extentTest) {
        Object[] parameters = iTestResult.getParameters();
        if (parameters.length > 0) {
            StringBuilder parametersInfo = new StringBuilder();
            for (Object parameter : parameters) {
                parametersInfo.append(parameter.toString()).append(",");
            }
            extentTest.log(Status.INFO, "Test Method Parameters: " + parametersInfo);
        }
    }

    private void configureReporter(ExtentSparkReporter extentSparkReporter) {
        extentSparkReporter.config().setDocumentTitle("Automation Report");
        extentSparkReporter.config().setReportName("Automation Test Results");
        extentSparkReporter.config().setTheme(Theme.STANDARD);
        extentSparkReporter.config().setEncoding("utf-8");
    }

    private String getTestName(ITestResult iTestResult) {
        Method testMethod = iTestResult.getMethod().getConstructorOrMethod().getMethod();
        Test testAnnotation = testMethod.getAnnotation(Test.class);

        String testName = null;
        if (testAnnotation != null) {
            testName = testAnnotation.testName();
        }

        if (testName == null || testName.isEmpty()) {
            testName = testMethod.getName();
        }
        return testName;
    }

    private ExtentTest getExtentTest(String methodName) {
        return testMap.get(methodName).get();
    }
}
