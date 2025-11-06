import {useEffect, useState} from 'react'
import './App.css'
import axios from 'axios'

function App() {

    const [urls, setURLS] = useState([]);
    const [url, setURL] = useState("");
    const [alias, setAlias] = useState("");

    useEffect(() => {
        axios.get('http://localhost:8080/url/urls')
            .then(response =>
                setURLS(response.data)
            )
    }, []);

    function postURL() {
        axios.post("http://localhost:8080/url/shorten", {
            url,
            alias
        })
            .then(response =>
            setURLS(response.data)
            )
            .catch(error =>
            console.log(error.data)
            )
    }

    function openURL() {
        axios.get("http://localhost:8080/url/" + alias, {
            validateStatus: status => status === 302
        })
            .then(response =>
                window.open(response.data, "_blank")
            )
            .catch(error =>
            console.log(error.data)
            )
    }


    return (
        <>
            <h1>Post new URL</h1>
            <form>
                <label>Url</label><br/>
                <input className="originalUrl" onChange={(e) => setURL(e.target.value)} name="url" value={url} type="text"></input><br/>
                <label>Alias</label><br/>
                <input className="urlAlias" onChange={(e) => setAlias(e.target.value)} name="alias" value={alias} type="text"></input><br/>
                <button onClick={postURL}>Send Request</button><br/>
            </form>


            <h1>Open Url from Alias</h1>
            <label>Alias</label><br/>
            <input className="urlAlias" onChange={(e) => setAlias(e.target.value)} name="alias" value={alias} type="text"></input><br/>
            <button onClick={openURL}>Send Request</button><br/>


            <h1>All URLS</h1>
            <table>
                <thead>
                <tr>
                    <th>Full URL</th>
                    <th>Short URL</th>
                    <th>Alias</th>
                </tr>
                </thead>
                <tbody>
                {urls.map((url) => {
                    return (
                        <tr>
                            <td>{url.originalUrl}</td>
                            <td>{url.shortenedUrl}</td>
                            <td>{url.alias}</td>
                        </tr>
                    )
                })}
                </tbody>
            </table>
        </>
    )
}

export default App
