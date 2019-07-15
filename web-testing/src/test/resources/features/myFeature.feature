
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

@wip
  Scenario: E-commerce demo
    Given I go to http://automationpractice.com/index.php
    When  I add the first bestseller item to my cart
    Then  I successfully added something to my cart