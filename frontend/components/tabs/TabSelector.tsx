import React from "react";
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
        {data.map((tab, index: number) => (
          <Tab key={index}>{tab.label}</Tab>
        ))}
      </TabList>
      <TabPanels>
        {data.map((tab, index: number) => (
          <TabPanel p={4} key={index}>
            {tab.content}
          </TabPanel>
        ))}
      </TabPanels>
    </Tabs>
  );
};
