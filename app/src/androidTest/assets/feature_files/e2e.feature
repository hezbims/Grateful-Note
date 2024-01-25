Feature: All Feature

  @e2e-test
  Scenario: e2e
    Given an user start the app
    When the user input three new positive emotions
    Then there is three list of card in home page
