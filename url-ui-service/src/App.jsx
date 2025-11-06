import {useEffect, useState} from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import axios from 'axios'

function App() {

    const [urls, setURLS] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/url/urls')
            .then(response =>
                setURLS(response.data)
            )
    }, []);

    return (
        <>
            <h1>All URLS</h1>
            <table>
                <tr>
                    <th>Full URL</th>
                    <th>Short URL</th>
                    <th>Alias</th>
                </tr>
                {urls.map((url) => {
                    return (
                        <tr>
                            <td>{url.originalUrl}</td>
                            <td>{url.shortenedUrl}</td>
                            <td>{url.alias}</td>
                        </tr>
                    )
                })}
            </table>
        </>
    )
}

export default App
