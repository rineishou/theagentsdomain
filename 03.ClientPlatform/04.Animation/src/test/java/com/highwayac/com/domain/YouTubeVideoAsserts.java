package com.highwayac.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class YouTubeVideoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertYouTubeVideoAllPropertiesEquals(YouTubeVideo expected, YouTubeVideo actual) {
        assertYouTubeVideoAutoGeneratedPropertiesEquals(expected, actual);
        assertYouTubeVideoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertYouTubeVideoAllUpdatablePropertiesEquals(YouTubeVideo expected, YouTubeVideo actual) {
        assertYouTubeVideoUpdatableFieldsEquals(expected, actual);
        assertYouTubeVideoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertYouTubeVideoAutoGeneratedPropertiesEquals(YouTubeVideo expected, YouTubeVideo actual) {
        assertThat(expected)
            .as("Verify YouTubeVideo auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertYouTubeVideoUpdatableFieldsEquals(YouTubeVideo expected, YouTubeVideo actual) {
        assertThat(expected)
            .as("Verify YouTubeVideo relevant properties")
            .satisfies(e -> assertThat(e.getWorkspace()).as("check workspace").isEqualTo(actual.getWorkspace()))
            .satisfies(e -> assertThat(e.getCreator()).as("check creator").isEqualTo(actual.getCreator()))
            .satisfies(e -> assertThat(e.getTheme()).as("check theme").isEqualTo(actual.getTheme()))
            .satisfies(e -> assertThat(e.getContent()).as("check content").isEqualTo(actual.getContent()))
            .satisfies(e -> assertThat(e.getBackgroundMusic()).as("check backgroundMusic").isEqualTo(actual.getBackgroundMusic()))
            .satisfies(e -> assertThat(e.getVideoTime()).as("check videoTime").isEqualTo(actual.getVideoTime()))
            .satisfies(e -> assertThat(e.getGender()).as("check gender").isEqualTo(actual.getGender()))
            .satisfies(e -> assertThat(e.getVideolanguage()).as("check videolanguage").isEqualTo(actual.getVideolanguage()))
            .satisfies(e -> assertThat(e.getSubtitles()).as("check subtitles").isEqualTo(actual.getSubtitles()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertYouTubeVideoUpdatableRelationshipsEquals(YouTubeVideo expected, YouTubeVideo actual) {}
}