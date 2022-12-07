import { Button } from '@chakra-ui/react'
import React from 'react'

export const TestButton = () => {

    const testFunc = () => {
        console.log("Test")
        fetch("/api/todo/checkbox/hello/test/false")
        .then(res => console.log(res.status))
    }

  return (
    <Button onClick={testFunc}>Test button</Button>
  )
}
