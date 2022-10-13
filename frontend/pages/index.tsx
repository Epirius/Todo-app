import type { NextPage } from 'next';
import { useSession } from 'next-auth/react';
import { Welcome } from '../components/Welcome';

const Home: NextPage = () => {
  const { data: session } = useSession();
  if (!session){
    return (
      <Welcome/>
    )
  }

  return (
    <div>
      hello
    </div>
  )
}

export default Home
