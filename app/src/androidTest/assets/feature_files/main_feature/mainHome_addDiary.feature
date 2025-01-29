@base_rule
Feature: Main Home - Add Diary
  Background:
    Given the user have 5 minimal diaries
    And user start the app
  Scenario: Diary Data Should Added Correctly
    When user click add new diary button
    And add title with 'New Title'
    And add description with 'New Desc'
    And add tag with 'Serenity'
    And click save new diary
    And confirm to save new diary
    Then the 1-th diary in main home is titled 'New Title'
    And the 1-th diary has tag 'Serenity'
    And there is '6' diaries in database
    And there is exactly one diary with:
      |title     | desc     | tag      | isFavorite | createdAt           | updatedAt           |
      |New Title | New Desc | Serenity | false      | 2020-01-15T00:00:00 | 2020-01-15T00:00:00 |