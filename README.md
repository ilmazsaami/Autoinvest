# Autoinvest
An auto investment app that invest automatically in given portfolios. Managed using Capital one API

## 💡Inspiration
In today's rapidly evolving world of business, marginalized people and students too, often face obstacles when it comes to investing. Limited support, knowledge, and busy schedules make it challenging to enter the investment realm. Additionally, the lack of substantial capital prevents us from exploring lucrative opportunities in major stocks and maximizing profits. This struggle is common among many students. Great potential lies within the stock markets, but how many of us have the time and money to put into investments?, Introducing Autoinvest through which anyone can start investing with no minimum portfolio balance, and no prior experience with investments needed. Our platform is well integrated to your bank account, and you decide how much money to invest in, with the option to withdraw any amount at any time. Sit back and watch your investments grow as we expertly manage a well diversified portfolio on your behalf.

## 💻 What it does
Autoinvest is a platform designed to inspire and empower investors. With Autoinvest, we aim to break down these barriers and provide a path to financial growth. Our platform offers a supportive environment, bridging the gap between limited resources and ambitious investment goals.Our platform is a form of "crowd investing" in that users pool their money together in order to make investments that they normally would not be able to on their own. There are two current challenges with investments: either one cannot afford to make the minimum investments (usually minimum 100 shares, or a minimum dollar investment amount), or one cannot afford to diversify their portfolio in order to minimize their risks and maximize their opportunities.

### Features:
  - Secure Authentication
  - Automation directly from bank account integrated using capital one Api.
  - Growth analytics.
  - No minimum investment requirement, making investing accessible to all.
  - Flexibility to invest any amount and withdraw funds anytime
  - Deposit and withdraw funds anytime.
  - Algorithmic investing
## 🦾How we built it
Our front-end client is an Android application, providing a user-friendly interface for our investment platform. The application allows users to conveniently interact with our services, making it easy to manage their investments on the go.

On the back end, we have developed our platform using Django, a powerful web framework. Django provides a robust foundation for handling the logic and data flow of our application, ensuring smooth operations and efficient backend management.

To seamlessly integrate users' bank accounts with our platform, we utilize the Capital One Nessie API. This API allows us to securely connect to users' bank accounts, enabling effortless transfer of funds between their bank and our investment platform. Users can easily deposit money into the platform or withdraw funds back to their bank accounts with a few simple steps.

To build our investment algorithm and select the most promising securities for our users' portfolios, we rely on the Yahoo Finance API. This API provides us with crucial key performance indicators and market data for various securities. We analyze this data to make informed investment decisions, ensuring that our users' portfolios are built with the best possible investments.

For executing actual trades on the selected securities, we leverage the Zipline API. Zipline is a Python-based API specifically designed for professional trading algorithms. It enables us to create and backtest our algorithms using ten years of historical data, allowing us to assess performance and risk indicators accurately.

To provide a comprehensive view of their investments, our Android client displays important information to users. This includes the ability to deposit funds into and withdraw from the investment platform seamlessly. Users can also view their book value, portfolio value, changes in value, and percentage changes, empowering them with a clear understanding of their investment performance.

Overall, our Android application serves as the gateway for users to interact with our investment platform, while our Django-powered back end leverages various APIs such as Capital One Nessie, Yahoo Finance, and Zipline to ensure seamless integration, data analysis, and efficient trading operations.

### 🎗️Challenges we ran into
One of the greatest challenges we encountered during the development of our project was the creation of our investment algorithms. It required us to conduct thorough research and analysis of various investment performance indicators. We delved into factors such as P/E (Price-to-Earnings) and P/B (Price-to-Book) ratios, as well as alpha-beta risk ratios. Understanding how book values are accurately calculated when dealing with multiple trades involving different securities at varying prices was also crucial. Although this process was initially complex, once we grasped the concepts, we were able to identify the essential characteristics of securities that would inform our investment decisions.
Integrating the Capital One API posed its own set of hurdles. We had to ensure a seamless and secure connection between our platform and users' bank accounts. This involved working with authentication protocols, establishing secure data transmission channels, and handling various error scenarios that could arise during the integration process.
Additionally, incorporating GraphView into our Android application presented its own complexities.
### 🏆Accomplishments that we're proud of
Completing the project before the deadline.
Learned lot about investments , finance and business analytics while implementing this app.
Integrating Capital one Nessie API.
Learning a lot: from workshops, sponsor technologies, competitors, MLHERS, and from each other.

### 📕What we learned
To work in team efficiently by dividing project
how the finance and stock markets operate
various android libraries like grapview,glide.
using capital one Nessie API.
We learned a lot not about investments and finance, and applied our learning to create algorithms.
🎈 What's next for Auto investment
Moving forward, our next steps involve the continuous development and enhancement of our algorithms to ensure adaptability to a wide range of market situations. By incorporating advanced machine learning techniques and analyzing real-time market data, we aim to improve the precision and effectiveness of our investment decisions. This will enable us to seize opportunities and navigate market fluctuations more effectively on behalf of our users.
