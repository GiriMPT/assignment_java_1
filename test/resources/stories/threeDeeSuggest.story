Scenario: when given postcode is in 3D area and baskets has got 3D products
Given a postcode along with following products in the basket:
|products|
|SPORTS|
|NEWS|
|MOVIES_1|
|MOVIES_2|
|KIDS|
When the postcode is in 3D area and basket has 3D products
Then return following addons
|addon|
|SPORTS_3D_ADD_ON|
|NEWS_3D_ADD_ON|
|MOVIES_3D_ADD_ON|


Scenario: when given postcode is in non 3D area and baskets has got 3D products
Given a postcode along with following products in the basket:
|products|
|SPORTS|
|NEWS|
|MOVIES_1|
When the postcode is in non-3D area and basket has 3D products
Then no addons should be returned

Scenario: when given postcode is in 3D area and baskets has got non 3D products
Given a postcode along with following products in the basket:
|products|
|VARIETY|
|KIDS|
When the postcode is in 3D area and basket has non 3D products
Then return zero addons

Scenario: when given postcode is invalid and baskets has got 3D products
Given a postcode along with following products in the basket:
|products|
|SPORTS|
|KIDS|
When the postcode is invalid
Then addons should not be returned


Scenario: when given postcode is in 3D area,baskets has got non 3D products and there is a technical failure
Given a postcode along with following products in the basket:
|products|
|SPORTS|
|KIDS|
When there is technical failure
Then empty addons should be returned







