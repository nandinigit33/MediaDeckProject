import React, { useState, useEffect } from 'react';
import Head from './components//Head';
import Header from './components/Header';
import Theme from './theme';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(true); // auth
  const [currentTheme, setCurrentTheme] = useState(Theme.DARK);

  useEffect(() => {
    const stored = localStorage.getItem("userTheme");
    if (stored && Theme[stored]) {
      setCurrentTheme(Theme[stored]);
    }
  }, []);

  return (
    <>
      <Head title="Mediadeck" cssPath={currentTheme.cssPath} />
      <Header isAuthenticated={isAuthenticated} navMode={currentTheme.navMode} />
    </>
  );
}

export default App;