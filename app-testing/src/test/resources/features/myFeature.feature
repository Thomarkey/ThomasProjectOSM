Feature: myFeature

  @wip
  Scenario: Creating a new task
    Given I launch the app
    When  I create a new task called Check out the refleqt booth
    And   I create a new task called Inform myself about testing
    And   I create a new task called Apply for an adventurous career
    And   I edit the last task to Apply for an challenging internship
    And   I create a new task called Check the other booths
    And   I delete the last task
    And   I check all the checkboxes
    Then  I wait for 3 seconds

