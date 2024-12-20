package com.bhasaka.newsportal.core.models;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class BundleActivatorImpl implements BundleActivator  {


    public static void main(String[] args) {
        ImageDetailsModel imageDetailsModel = new ImageDetailsModel();
        System.out.println(imageDetailsModel);

    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {

    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}
