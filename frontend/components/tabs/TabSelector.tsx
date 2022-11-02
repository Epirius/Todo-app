import React, { useEffect, useState } from "react";
import {
  Tabs,
  TabList,
  TabPanels,
  Tab,
  TabPanel,
  Button,
  Spacer,
} from "@chakra-ui/react";
import { CreateTab } from "./CreateTab";

export const TabSelector = () => {
  const [tabs, setTabs] = useState([
    { tabName: "Today", email: "techtvids@gmail.com" },
  ]);
  useEffect(() => {
    pullTabs();
  }, []);

  const pullTabs = async () => {
    fetch("/api/tabs/getTabs")
      .then((res) => res.json())
      //.then(res => console.log(res))
      .then((res) => setTabs(res));
    //.then(data => setTabs(data))
  };

  const data = [
    {
      label: "No Tabs",
      content: "No tabs could be found, try creating one",
    },
  ];

  return (
    <Tabs>
      <TabList
        overflowX="scroll"
        overflowY="hidden"
        css={{
          "&::-webkit-scrollbar": {
            width: "4px",
            height: "4px"
          },
          "&::-webkit-scrollbar-track": {
            width: "6px",
          },
          "&::-webkit-scrollbar-thumb": {
            background: "#8ccef0",
            borderRadius: "24px",
          },
        }}
      >
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
      <Button onClick={pullTabs}>pull</Button>
      <CreateTab>create tab</CreateTab>
    </Tabs>
  );
};
