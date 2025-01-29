@base_rule
Feature: Main Home - Delete Diary

  Background:
    Given the user have "50" minimal diaries
    And user start the app

  Scenario: Deleted diary must disappear
    When the user click the delete icon at the '25'-th diary with title 'what-26'
    And confirm the diary deletion
    Then the 25-th diary in main home is titled 'what-26'
    And diary titled 'what-26' will not be exist in the database