@wip
Feature: This is my feature

  Scenario Outline: Go to google - search for <search>
    Given I go to https://www.google.be
    When  I search for <search>
    Then  The first result is <result>
    And   I searched for <search>
    And   I found <result>

    Examples:
      | search   | result                                                 |
      | refleqt  | REFLEQT \| Refleqt                                     |
      | cloudway | Driving digital agility in the cloud \| Cloudway       |
      | xplore   | Xplore Group \| Home \| Connect, create and make sense |
      | xti      | XTi: Welkom                                            |