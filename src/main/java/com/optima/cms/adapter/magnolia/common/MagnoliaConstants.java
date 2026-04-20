package com.optima.cms.adapter.magnolia.common;

public class MagnoliaConstants {
    public static final String MOCK_PLANS_JSON = """
            [
              {
                "id": "mgnl-plan-gold-001",
                "createdAt": "2026-01-01T12:00:00.000Z",
                "updatedAt": "2026-02-01T12:00:00.000Z",
                "description": "Premium 5G plan with unlimited data, calls and SMS.",
                "name": "Gold 5G Unlimited",
                "externalId": "PLAN-GOLD-5G-001",
                "price": [
                  {
                    "id": "1",
                    "type": "recurring",
                    "amount": {
                      "value": "79.99",
                      "currency": "USD"
                    }
                  }
                ],
                "characteristics": {
                  "allowance": [
                    { "type": "data", "value": "Unlimited" },
                    { "type": "voice", "value": "Unlimited" },
                    { "type": "sms", "value": "Unlimited" },
                    { "type": "ott", "value": "Netflix, Spotify" }
                  ],
                  "features": [
                    { "value": "5G", "key": "Network" },
                    { "value": "50GB", "key": "Hotspot" },
                    { "value": "24 months", "key": "Contract" },
                    { "value": "100 min", "key": "International" }
                  ]
                },
                "extension": [
                  { "value": "1", "key": "priority" },
                  { "value": "Most Popular", "key": "badge" }
                ],
                "attachment": [
                  {
                    "id": "plan-icon",
                    "name": "Gold Plan Icon",
                    "attachmentType": "icon"
                  },
                  {
                    "id": "plan-details-html",
                    "name": "Plan Details",
                    "content": "<div>Full plan details HTML...</div>",
                    "attachmentType": "other"
                  }
                ]
              },
              {
                "description": "Test Test",
                "name": "ABC",
                "externalId": "Test",
                "price": [
                  {
                    "id": "1",
                    "type": "recurring",
                    "amount": { "currency": "USD", "value": "100" }
                  },
                  {
                    "id": "2",
                    "type": "recurring",
                    "amount": { "currency": "EUR", "value": "1000" }
                  }
                ],
                "characteristics": {
                  "allowance": [
                    { "type": "data", "value": "Unlimited" },
                    { "type": "voice", "value": "Unlimited" }
                  ],
                  "features": [
                    { "value": "123", "key": "ABC" }
                  ]
                },
                "extension": [
                  { "value": "Test2", "key": "1" }
                ]
              }
            ]
            """;

    public static final String MOCK_FIND_ALL = "{\n" +
            "\t\"docs\": [\n" +
            "\t\t{\n" +
            "\t\t\t\"createdAt\": \"2026-01-22T10:29:21.854Z\",\n" +
            "\t\t\t\"updatedAt\": \"2026-02-18T20:44:23.749Z\",\n" +
            "\t\t\t\"externalId\": \"2000253628\",\n" +
            "\t\t\t\"name\": \"Unlimited PlanDD\",\n" +
            "\t\t\t\"attachment\": [\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"id\": \"promo-unlimited-plan-001\",\n" +
            "\t\t\t\t\t\"name\": \"Premium extras\",\n" +
            "\t\t\t\t\t\"attachmentType\": \"text\",\n" +
            "\t\t\t\t\t\"content\": \"<ul><li>Free streaming subscription for 3 months</li><li>Priority customer support 24/7</li><li>Unlimited hotspot tethering</li></ul>\",\n" +
            "\t\t\t\t\t\"validFor\": {},\n" +
            "\t\t\t\t\t\"extension\": [\n" +
            "\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\"key\": \"position\",\n" +
            "\t\t\t\t\t\t\t\"value\": \"promotion\",\n" +
            "\t\t\t\t\t\t\t\"id\": \"ext-promo-up-001\"\n" +
            "\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"price\": [],\n" +
            "\t\t\t\"characteristics\": {\n" +
            "\t\t\t\t\"allowance\": [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"data\",\n" +
            "\t\t\t\t\t\t\"value\": \"Unlimited\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fbd5c776b628938a09e5\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"sms\",\n" +
            "\t\t\t\t\t\t\"value\": \"Unlimited\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fbe1c776b628938a09e6\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"voice\",\n" +
            "\t\t\t\t\t\t\"value\": \"Unlimited\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fbe5c776b628938a09e7\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"roaming-data\",\n" +
            "\t\t\t\t\t\t\"value\": \"Unlimited\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fbe9c776b628938a09e8\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"roaming-voice\",\n" +
            "\t\t\t\t\t\t\"value\": \"Unlimited\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fbf1c776b628938a09e9\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"international-calls\",\n" +
            "\t\t\t\t\t\t\"value\": \"Unlimited\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fbf6c776b628938a09ea\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t],\n" +
            "\t\t\t\t\"features\": [],\n" +
            "\t\t\t\t\"extension\": []\n" +
            "\t\t\t},\n" +
            "\t\t\t\"extension\": [],\n" +
            "\t\t\t\"id\": \"6971fc0103cc513f4e2794d1\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"createdAt\": \"2026-01-22T10:27:45.710Z\",\n" +
            "\t\t\t\"updatedAt\": \"2026-02-18T20:44:23.373Z\",\n" +
            "\t\t\t\"externalId\": \"2000251697\",\n" +
            "\t\t\t\"name\": \"Economy Plan\",\n" +
            "\t\t\t\"attachment\": [\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"id\": \"promo-economy-plan-001\",\n" +
            "\t\t\t\t\t\"name\": \"On recharge\",\n" +
            "\t\t\t\t\t\"attachmentType\": \"text\",\n" +
            "\t\t\t\t\t\"content\": \"<ul><li>Extra 10,000 MB data on every recharge</li><li>Free 200 SMS to any network</li><li>10% cashback on first recharge</li></ul>\",\n" +
            "\t\t\t\t\t\"validFor\": {},\n" +
            "\t\t\t\t\t\"extension\": [\n" +
            "\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\"key\": \"position\",\n" +
            "\t\t\t\t\t\t\t\"value\": \"promotion\",\n" +
            "\t\t\t\t\t\t\t\"id\": \"ext-promo-ep-001\"\n" +
            "\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"price\": [],\n" +
            "\t\t\t\"characteristics\": {\n" +
            "\t\t\t\t\"allowance\": [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"data\",\n" +
            "\t\t\t\t\t\t\"value\": \"20,000 MB\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fb49c776b628938a09e0\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"voice\",\n" +
            "\t\t\t\t\t\t\"value\": \"Unlimited voice calls\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fb53c776b628938a09e1\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"sms\",\n" +
            "\t\t\t\t\t\t\"value\": \"1000 SMS/Day\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fb59c776b628938a09e2\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"roaming-data\",\n" +
            "\t\t\t\t\t\t\"value\": \"5,000 MB\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fb64c776b628938a09e3\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t],\n" +
            "\t\t\t\t\"features\": [],\n" +
            "\t\t\t\t\"extension\": []\n" +
            "\t\t\t},\n" +
            "\t\t\t\"extension\": [],\n" +
            "\t\t\t\"id\": \"6971fba103cc513f4e279489\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"createdAt\": \"2026-01-22T10:25:04.777Z\",\n" +
            "\t\t\t\"updatedAt\": \"2026-02-19T17:23:21.718Z\",\n" +
            "\t\t\t\"externalId\": \"2000251334\",\n" +
            "\t\t\t\"name\": \"Basic Plan\",\n" +
            "\t\t\t\"attachment\": [\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"id\": \"promo-basic-plan-001\",\n" +
            "\t\t\t\t\t\"name\": \"Weekend bonus\",\n" +
            "\t\t\t\t\t\"attachmentType\": \"text\",\n" +
            "\t\t\t\t\t\"content\": \"<ul><li>Double data on weekends (20,000 MB)</li><li>Free 100 international SMS</li><li>Bonus 50 minutes to all networks</li></ul>\",\n" +
            "\t\t\t\t\t\"validFor\": {},\n" +
            "\t\t\t\t\t\"extension\": [\n" +
            "\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\"key\": \"position\",\n" +
            "\t\t\t\t\t\t\t\"value\": \"promotion\",\n" +
            "\t\t\t\t\t\t\t\"id\": \"ext-promo-bp-001\"\n" +
            "\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"id\": \"bg-basic-plan-001\",\n" +
            "\t\t\t\t\t\"name\": \"Background Image\",\n" +
            "\t\t\t\t\t\"attachmentType\": \"picture\",\n" +
            "\t\t\t\t\t\"url\": \"https://images.unsplash.com/photo-1557683316-973673baf926?w=800&q=80\",\n" +
            "\t\t\t\t\t\"validFor\": {},\n" +
            "\t\t\t\t\t\"extension\": [\n" +
            "\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\"key\": \"position\",\n" +
            "\t\t\t\t\t\t\t\"value\": \"background-image\",\n" +
            "\t\t\t\t\t\t\t\"id\": \"ext-bg-bp-001\"\n" +
            "\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"price\": [],\n" +
            "\t\t\t\"characteristics\": {\n" +
            "\t\t\t\t\"allowance\": [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"data\",\n" +
            "\t\t\t\t\t\t\"value\": \"10,000 MB\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fab2c776b628938a09dd\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"voice\",\n" +
            "\t\t\t\t\t\t\"value\": \"100 Minutes\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fac2c776b628938a09de\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"sms\",\n" +
            "\t\t\t\t\t\t\"value\": \"10 SMS/Day\",\n" +
            "\t\t\t\t\t\t\"extension\": [\n" +
            "\t\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\t\"key\": \"Gift\",\n" +
            "\t\t\t\t\t\t\t\t\"value\": \"Valid\",\n" +
            "\t\t\t\t\t\t\t\t\"id\": \"699746f68b094b5e9a1f1a8f\"\n" +
            "\t\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t\"id\": \"6971fae9c776b628938a09df\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t],\n" +
            "\t\t\t\t\"features\": [],\n" +
            "\t\t\t\t\"extension\": []\n" +
            "\t\t\t},\n" +
            "\t\t\t\"extension\": [],\n" +
            "\t\t\t\"description\": \"General basic plan\",\n" +
            "\t\t\t\"id\": \"6971fb0003cc513f4e279440\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"createdAt\": \"2026-01-22T10:09:33.126Z\",\n" +
            "\t\t\t\"updatedAt\": \"2026-02-18T20:32:20.343Z\",\n" +
            "\t\t\t\"externalId\": \"2000265716\",\n" +
            "\t\t\t\"name\": \"Budget Talk\",\n" +
            "\t\t\t\"attachment\": [\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"id\": \"promo-budget-talk-001\",\n" +
            "\t\t\t\t\t\"name\": \"On activation\",\n" +
            "\t\t\t\t\t\"attachmentType\": \"text\",\n" +
            "\t\t\t\t\t\"content\": \"<ul><li>Bonus 5,000 MB data for the first 30 days</li><li>Free 5G network access included</li><li>Unlimited calls to 3 favorite numbers</li></ul>\",\n" +
            "\t\t\t\t\t\"validFor\": {},\n" +
            "\t\t\t\t\t\"extension\": [\n" +
            "\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\"key\": \"position\",\n" +
            "\t\t\t\t\t\t\t\"value\": \"promotion\",\n" +
            "\t\t\t\t\t\t\t\"id\": \"ext-promo-bt-001\"\n" +
            "\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"price\": [],\n" +
            "\t\t\t\"characteristics\": {\n" +
            "\t\t\t\t\"allowance\": [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"data\",\n" +
            "\t\t\t\t\t\t\"value\": \"10,000 MB\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971f70ec2bf564002b259b8\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"voice\",\n" +
            "\t\t\t\t\t\t\"value\": \"Unlimited voice calls\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971f72fc2bf564002b259b9\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"sms\",\n" +
            "\t\t\t\t\t\t\"value\": \"50 SMS/Day\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971f73ec2bf564002b259ba\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"type\": \"other\",\n" +
            "\t\t\t\t\t\t\"value\": \"Free 5G Network access\",\n" +
            "\t\t\t\t\t\t\"extension\": [],\n" +
            "\t\t\t\t\t\t\"id\": \"6971f74ac2bf564002b259bb\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t],\n" +
            "\t\t\t\t\"features\": [],\n" +
            "\t\t\t\t\"extension\": []\n" +
            "\t\t\t},\n" +
            "\t\t\t\"extension\": [],\n" +
            "\t\t\t\"id\": \"6971f75d03cc513f4e2793d7\"\n" +
            "\t\t}\n" +
            "\t],\n" +
            "\t\"totalDocs\": 4,\n" +
            "\t\"limit\": 5,\n" +
            "\t\"totalPages\": 1,\n" +
            "\t\"page\": 1,\n" +
            "\t\"pagingCounter\": 1,\n" +
            "\t\"hasPrevPage\": false,\n" +
            "\t\"hasNextPage\": false,\n" +
            "\t\"prevPage\": null,\n" +
            "\t\"nextPage\": null\n" +
            "}";
}
