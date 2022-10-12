import Link from "next/link"

const Header = () => {
  return (
    <div>
        <div>Todo app</div>
        <nav>
            <ul>
                <li>
                    <Link href='/'>App</Link>
                </li>
                <li>
                    <Link href='/about'>About</Link>
                </li>
            </ul>
        </nav>
    </div>
  )
}

export default Header
