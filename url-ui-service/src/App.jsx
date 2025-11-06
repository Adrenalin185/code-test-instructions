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
        axios.post('http://localhost:8080/url/shorten', {
            url,
            alias
        })
            .then(response =>
            setURLS(response.data)
            )
            .catch(error =>
            console.log('Url failed to shorten:', error.data)
            )
        window.location.reload();
    }

    function openURL() {
        axios.get('http://localhost:8080/url/'+ alias, {
            validateStatus: status => status === 302
        })
            .then(response =>
                window.open(response.data, "_blank")
            )
            .catch(error =>
            console.log('Failed to retrieve Url:', error.data)
            )
    }

    function deleteURL () {
        axios.delete('http://localhost:8080/url/'+ alias)
            .then(response => {
                console.log('Deleted Successful:', response.data)
            })
            .catch(error => {
                console.log('Error with deletion:', error.data)
            })
        window.location.reload();
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

            <h1>Delete URL from Alias</h1>
            <label>Alias</label><br/>
            <input className="urlAlias" onChange={(e) => setAlias(e.target.value)} name="alias" value={alias} type="text"></input><br/>
            <button onClick={deleteURL}>Send Request</button><br/>



            <h1>All URLS</h1>
            <table style={{"width":"100%"}}>
                <thead>
                <tr>
                    <th style={{"width":"33%"}}>Full URL</th>
                    <th style={{"width":"33%"}}>Short URL</th>
                    <th style={{"width":"33%"}}>Alias</th>
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
