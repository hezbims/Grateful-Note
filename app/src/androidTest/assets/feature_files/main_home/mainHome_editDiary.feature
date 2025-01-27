@base_rule
Feature: Main Home - Edit Diary

  Background:
    Given the user have "50" minimal diaries
    And user start the app

  Scenario: Edited Diary Must Be Visible on Top
    When the user try edit the '25'-th diary with title 'what-26'
    And edit title with 'Title updated'
    And edit desc with 'desc updated'
    And user go back after edit the diary
    Then the '1'-th diary in main home is titled 'Title updated'