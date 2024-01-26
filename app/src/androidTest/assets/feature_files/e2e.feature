Feature: All Feature

  @e2e-test
  Scenario: End to End Test
    Given an user start the app
    When the user input three new positive emotions
    Then there is three list of card in home page
    When the user edit the second positive emotion
    Then the second positive emotion edited
