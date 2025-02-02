@base_rule
Feature: Main Home - Edit Diary

  Background:
    Given the user have 50 minimal diaries
    And user start the app

  Scenario: Edited Diary Must Be Visible on Top After Editing
    When the user go to edit the 25-th diary with title 'what-26'
    And edit title with 'Title updated'
    And edit desc with 'desc updated'
    And user go back after edit the diary
    Then the 1-th diary in main home is titled "Title updated"

  Scenario: Diary Data Shouldn't Behave as Edited When User Doesn't Type Anything
    When the user go to edit the 25-th diary with title 'what-26'
    And user go back after edit the diary
    Then the 25-th diary in main home is titled "what-26"

  Scenario: Edit Diary Screen Should Display Data Correctly
    When the user go to edit the 2-th diary with title 'what-49'
    Then the diary title in edit screen is 'what-49'
    And the diary description in edit screen is 'why-49'

  Scenario: Toggle Favorite Icon Should Make Diary As Favorite
    When toggle favorite icon of 2-th diary with title 'what-49'
    Then 2-th diary with title 'what-49' should have active favorite icon
    And there is exactly one diary with:
      | title   | isFavorite |
      | what-49 | true       |
