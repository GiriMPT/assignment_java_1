package com.bskyb.onlinestore;

public class StoryPathAccessor {
    public static String getStory(Object target) {
        Story annotation = target.getClass().getAnnotation(Story.class);
        if (annotation == null) {
            throw new IllegalArgumentException(target + " is not annotated with " + Story.class.getName());
        }

        return annotation.classpath();
    }
}

