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
import { TabPage } from "./TabPage";
import { CreateForm } from "../CreateForm";

interface tabObj{
  tabName: String,
  email: String
}

export const TabSelector = () => {
  const [tabs, setTabs] = useState(new Array<tabObj>());
  useEffect(() => {
    pullTabs();
  }, []);

  const pullTabs = () => {
    fetch("/api/tabs/getTabs")
      .then(res => {
        if(res.status !== 200) throw Error("response not 200");
        return res.json()
      })
      .then((res) => setTabs(res))
      .catch((err) => {
        console.log("could not fetch the tabs from backend. error: " + err);
      });
  };

  const createTabCallback = async (slug: String) => {
    fetch("/api/tabs/create/" + slug)
    .then(res => {
      if (res.status === 200){
        pullTabs()
      } else {
        throw Error("response not 200");
      }
    });
  }

  const data = [
    {
      label: "No Tabs",
      content: "No tabs could be found, try creating one",
    },
  ];

  return (
    <Tabs variant="enclosed" >
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
        <CreateForm callbackFunc={createTabCallback} type='tab'/>
      </TabList>

      <TabPanels>
        {tabs.map((tab, index: number) => (
          <TabPanel p={4} key={index}>
            <TabPage tabName={tab.tabName} pullTabsFromServer={pullTabs} />
          </TabPanel>
        ))}
      </TabPanels>
      <Button onClick={pullTabs}>pull</Button>
    </Tabs>
  );
};
