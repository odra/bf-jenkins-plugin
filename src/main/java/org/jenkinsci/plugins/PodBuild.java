package org.jenkinsci.plugins;

import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.ArgumentListBuilder;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PodBuild extends Builder implements SimpleBuildStep {
    public String repo;

    @DataBoundConstructor
    public PodBuild(String repo) {
        this.repo = repo;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        if (this.repo == null) {
            throw new AbortException("Repo property is null");
        }

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        Launcher.ProcStarter proc = launcher.launch();

        ArgumentListBuilder args = new ArgumentListBuilder();
        args.add("/usr/local/bin/pod");
        args.add("repo");
        args.add(this.repo);
        args.add("update");

        int rc = proc
                .cmds(args)
                .stdout(stdout)
                .stderr(stderr)
                .join();

        if (rc == 0) {
            taskListener.getLogger().write(stdout.toByteArray());
        } else {
            taskListener.getLogger().write(stderr.toByteArray());
            throw new AbortException("Error while executing pod command");
        }
    }

    @Override
    public PodBuild.DescriptorImpl getDescriptor() {

        return (PodBuild.DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        @Override
        public String getDisplayName() {
            return "Pod Update";
        }

        protected DescriptorImpl(Class<? extends Builder> clazz) {
            super(clazz);
        }

        public DescriptorImpl() {
            load();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

    }
}
