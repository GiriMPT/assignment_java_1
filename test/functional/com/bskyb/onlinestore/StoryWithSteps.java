package com.bskyb.onlinestore;

import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;
import org.apache.commons.io.IOUtils;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.PrintStreamStepdocReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.jbehave.core.reporters.Format.*;

@RunWith(JUnitReportingRunner.class)
public abstract class StoryWithSteps extends JUnitStories {
    private final CrossReference xref = new CrossReference();

    StoryWithSteps() {
        configuredEmbedder().embedderControls().doIgnoreFailureInView(true).useStoryTimeoutInSecs(2000);
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withDefaultFormats()
                        .withFormats(CONSOLE, TXT, HTML_TEMPLATE, IDE_CONSOLE)
                        .withFailureTrace(true)
                        .withCrossReference(xref))
                .useStepdocReporter(new PrintStreamStepdocReporter())
                .useStoryLoader(getStoryLoader());
    }

    private StoryLoader getStoryLoader() {
        return new StoryLoader() {
            @Override
            public String loadStoryAsText(String storyClasspath) {
                try {
                    return IOUtils.toString(getClass().getResourceAsStream(
                            storyClasspath));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), this);
    }

    @Override
    public List<String> storyPaths() {
        return Arrays.asList(StoryPathAccessor.getStory(this));
    }
}
