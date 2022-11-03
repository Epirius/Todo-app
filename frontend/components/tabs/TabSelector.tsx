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
import { Todo } from "../todo/Todo";

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
      .then((res) => setTabs(res))
      .catch((err) => {
        console.log("could not fetch the tabs from backend. error: " + err);
        setTabs([
          {
            tabName: "Error",
            email: "Could not fetch the tabs from the backend",
          },
        ]);
      });
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
            height: "4px",
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
        
        <Spacer />
        <CreateTab pullTabsFromServer={pullTabs}>create tab</CreateTab>
      </TabList>

      <TabPanels>
        {tabs.map((tab, index: number) => (
          <TabPanel p={4} key={index}>
            {tab.email + " -- tab index: " + index /*TODO*/}
            <Todo />
          </TabPanel>
        ))}
      </TabPanels>
      <Button onClick={pullTabs}>pull</Button>
    </Tabs>
  );
};
