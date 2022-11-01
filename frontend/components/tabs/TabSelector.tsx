import React, { useState } from "react";
import {
  Tabs,
  TabList,
  TabPanels,
  Tab,
  TabPanel,
  Button,
  Spacer,
} from "@chakra-ui/react";

export const TabSelector = () => {
  const [tabs, setTabs] = useState([
    { tabName: 'Today', email: 'techtvids@gmail.com' },
  ]);

  const pull = async () => {
    fetch('/api/tabs/getTabs')
    .then(res => res.json())
    //.then(res => console.log(res))
    .then(res => setTabs(res))
    //.then(data => setTabs(data))
  }

  const data = [
    {
      label: "Today",
      content: "Perhaps the greatest dish ever invented.",
    },
    {
      label: "School",
      content:
        "Perhaps the surest dish ever invented but fills the stomach more than rice.",
    },
  ];

  return (
    <Tabs>
      <TabList>
        {tabs.map((tab, index: number) => (
          <Tab key={index}>{tab.tabName}</Tab>
        ))}
      </TabList>
      <TabPanels>
        {tabs.map((tab, index: number) => (
          <TabPanel p={4} key={index}>
            {tab.email + " -- tab index: " + index /*TODO*/}
          </TabPanel>
        ))}
      </TabPanels>
      <Button onClick={pull}>pull</Button>
    </Tabs>
  );
};
