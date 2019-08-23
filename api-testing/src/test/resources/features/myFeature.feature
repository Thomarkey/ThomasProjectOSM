Feature: Example feature

  @wip
  Scenario Outline: Performing some calls
    Given I create a pet with:
      | id   | <id>   |
      | name | <name> |
    Then  The created pet is named <name>

    Examples:
      | id  | name  |
      | 300 | Karl  |
      | 400 | Jefke |


  Scenario: [EXAMPLE] Define parameter
    Given New color green is printed


  Scenario: [EXAMPLE] MultiData Form request (Uploading CSV / OAuth Calls)
    Given I perform a multiData form request


  Scenario: [EXAMPLE] negative cases
    Then  The system returns error message with:
      | status  | <status>  |
      | code    | <code>    |
      | message | <message> |
    And   The system returns field errors with:
      | field    | code         | message         |
      | <field>  | <fieldCode>  | <fieldMessage>  |
      | <field2> | <fieldCode2> | <fieldMessage2> |
