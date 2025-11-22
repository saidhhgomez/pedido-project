import { useState } from "react"

export default function App() {
  const [counter,setCounter]=useState(0);

  return (
    <>
     <h1>{counter}</h1>
     <button onClick={()=>setCounter(counter+ 1)}>aumentar</button>
    </>
  )
}

