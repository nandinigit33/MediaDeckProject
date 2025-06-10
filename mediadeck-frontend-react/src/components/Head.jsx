import React from 'react';
import { Helmet } from 'react-helmet';

const Head = ({ title, cssPath }) => (
  <Helmet>
    <meta charSet="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    {cssPath && <link rel="stylesheet" href={cssPath} />}
    <title>{title}</title>
  </Helmet>
);

export default Head;