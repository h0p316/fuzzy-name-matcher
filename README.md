# fuzzy-name-matcher
Fuzzy name matching service using Jaccard similarity, Levenshtein distance, and word permutations to deliver realistic similarity scores between customer and beneficiary names. Built with Java 17, Spring Boot, and Apache Commons Text.


# Fuzzy Name Matcher

A Spring Boot service for fuzzy name matching that calculates a **realistic similarity score** between two names using:
- **Word-level Jaccard similarity** (checks how many words overlap)
- **Levenshtein distance** (measures character-level edits needed to match)
- **Word permutations** (handles name order changes)

This is especially useful in **banking / KYC / customer verification** workflows where names may be slightly different due to typos, ordering, or missing parts.

---

## ✨ Features
- **Realistic scoring** (0.00 – 100.00%)
- Handles:
  - Typo tolerance  
  - Missing middle names  
  - Different word orders  
  - Extra spaces and special characters
- REST API with **OpenAPI/Swagger UI** for easy testing
- Built with **Java 17 + Spring Boot 3** for modern performance

---

## 📊 Scoring Logic

**Step 1:** Normalize  
- Lowercase  
- Remove special characters  
- Trim spaces  

**Step 2:** Generate permutations of the customer name (to handle name order changes)  

**Step 3:** For each permutation:
- Calculate **Jaccard similarity** on words  
- Calculate **Levenshtein similarity** on characters  
- Take the **average** of both scores  

**Step 4:** Select the **highest** score from all permutations and return as a percentage.

## 🔄 Matching Logic Flow

```mermaid
flowchart LR
    A[Customer Name] --> B[Normalize]
    B --> C[Generate Word Permutations]
    C --> D[Calculate Jaccard Similarity]
    C --> E[Calculate Levenshtein Similarity]
    D --> F[Average Scores]
    E --> F
    F --> G[Select Highest Score]
    G --> H[Return % Match in API Response]

## 🚀 Getting Started
---

### 1️⃣ Clone the repo
```bash
git clone https://github.com/h0p316/fuzzy-name-matcher.git
cd FuzzyNameMatch

2️⃣ Build the project
bash

mvn clean install
3️⃣ Run the Spring Boot app
bash

mvn spring-boot:run
The service will start at:

arduino

http://localhost:8080
📡 API Usage
Endpoint
bash

POST /api/fuzzy-match
Request Body
json

{
  "customer": "VISHAL KUMAR",
  "beneficiary": "VISHAL KUMA"
}
Response
json

{
  "beneficiary": "VISHAL KUMA",
  "score": 96.67
}
📖 Swagger UI
Once the app is running, visit:

bash

http://localhost:8080/swagger-ui.html
to test the API interactively.

🛠 Tech Stack
Java 17

Spring Boot 3

Apache Commons Text (LevenshteinDistance)

Springdoc OpenAPI (Swagger UI)

Lombok (boilerplate reduction)

Maven


