begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
operator|.
name|Funnels
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
operator|.
name|Hasher
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
operator|.
name|Hashing
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|ByteStreams
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|Die
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|IoUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|ConsoleUI
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|SitePaths
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ProxySelector
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|HttpSupport
import|;
end_import

begin_comment
comment|/** Get optional or required 3rd party library files into $site_path/lib. */
end_comment

begin_class
DECL|class|LibraryDownloader
class|class
name|LibraryDownloader
block|{
DECL|field|ui
specifier|private
specifier|final
name|ConsoleUI
name|ui
decl_stmt|;
DECL|field|lib_dir
specifier|private
specifier|final
name|Path
name|lib_dir
decl_stmt|;
DECL|field|remover
specifier|private
specifier|final
name|StaleLibraryRemover
name|remover
decl_stmt|;
DECL|field|required
specifier|private
name|boolean
name|required
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|jarUrl
specifier|private
name|String
name|jarUrl
decl_stmt|;
DECL|field|sha1
specifier|private
name|String
name|sha1
decl_stmt|;
DECL|field|remove
specifier|private
name|String
name|remove
decl_stmt|;
DECL|field|needs
specifier|private
name|List
argument_list|<
name|LibraryDownloader
argument_list|>
name|needs
decl_stmt|;
DECL|field|neededBy
specifier|private
name|LibraryDownloader
name|neededBy
decl_stmt|;
DECL|field|dst
specifier|private
name|Path
name|dst
decl_stmt|;
DECL|field|download
specifier|private
name|boolean
name|download
decl_stmt|;
comment|// download or copy
DECL|field|exists
specifier|private
name|boolean
name|exists
decl_stmt|;
DECL|field|skipDownload
specifier|private
name|boolean
name|skipDownload
decl_stmt|;
annotation|@
name|Inject
DECL|method|LibraryDownloader (ConsoleUI ui, SitePaths site, StaleLibraryRemover remover)
name|LibraryDownloader
parameter_list|(
name|ConsoleUI
name|ui
parameter_list|,
name|SitePaths
name|site
parameter_list|,
name|StaleLibraryRemover
name|remover
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
name|this
operator|.
name|lib_dir
operator|=
name|site
operator|.
name|lib_dir
expr_stmt|;
name|this
operator|.
name|remover
operator|=
name|remover
expr_stmt|;
name|this
operator|.
name|needs
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
DECL|method|setName (String name)
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|setJarUrl (String url)
name|void
name|setJarUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|jarUrl
operator|=
name|url
expr_stmt|;
name|download
operator|=
name|jarUrl
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
expr_stmt|;
block|}
DECL|method|setSHA1 (String sha1)
name|void
name|setSHA1
parameter_list|(
name|String
name|sha1
parameter_list|)
block|{
name|this
operator|.
name|sha1
operator|=
name|sha1
expr_stmt|;
block|}
DECL|method|setRemove (String remove)
name|void
name|setRemove
parameter_list|(
name|String
name|remove
parameter_list|)
block|{
name|this
operator|.
name|remove
operator|=
name|remove
expr_stmt|;
block|}
DECL|method|addNeeds (LibraryDownloader lib)
name|void
name|addNeeds
parameter_list|(
name|LibraryDownloader
name|lib
parameter_list|)
block|{
name|needs
operator|.
name|add
argument_list|(
name|lib
argument_list|)
expr_stmt|;
block|}
DECL|method|setSkipDownload (boolean skipDownload)
name|void
name|setSkipDownload
parameter_list|(
name|boolean
name|skipDownload
parameter_list|)
block|{
name|this
operator|.
name|skipDownload
operator|=
name|skipDownload
expr_stmt|;
block|}
DECL|method|downloadRequired ()
name|void
name|downloadRequired
parameter_list|()
block|{
name|setRequired
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|download
argument_list|()
expr_stmt|;
block|}
DECL|method|downloadOptional ()
name|void
name|downloadOptional
parameter_list|()
block|{
name|required
operator|=
literal|false
expr_stmt|;
name|download
argument_list|()
expr_stmt|;
block|}
DECL|method|setRequired (boolean r)
specifier|private
name|void
name|setRequired
parameter_list|(
name|boolean
name|r
parameter_list|)
block|{
name|required
operator|=
name|r
expr_stmt|;
for|for
control|(
name|LibraryDownloader
name|d
range|:
name|needs
control|)
block|{
name|d
operator|.
name|setRequired
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|download ()
specifier|private
name|void
name|download
parameter_list|()
block|{
if|if
condition|(
name|skipDownload
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|jarUrl
operator|==
literal|null
operator|||
operator|!
name|jarUrl
operator|.
name|contains
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid JarUrl for "
operator|+
name|name
argument_list|)
throw|;
block|}
specifier|final
name|String
name|jarName
init|=
name|jarUrl
operator|.
name|substring
argument_list|(
name|jarUrl
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|jarName
operator|.
name|contains
argument_list|(
literal|"/"
argument_list|)
operator|||
name|jarName
operator|.
name|contains
argument_list|(
literal|"\\"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid JarUrl: "
operator|+
name|jarUrl
argument_list|)
throw|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|jarName
expr_stmt|;
block|}
name|dst
operator|=
name|lib_dir
operator|.
name|resolve
argument_list|(
name|jarName
argument_list|)
expr_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|dst
argument_list|)
condition|)
block|{
name|exists
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|shouldGet
argument_list|()
condition|)
block|{
name|doGet
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|exists
condition|)
block|{
for|for
control|(
name|LibraryDownloader
name|d
range|:
name|needs
control|)
block|{
name|d
operator|.
name|neededBy
operator|=
name|this
expr_stmt|;
name|d
operator|.
name|downloadRequired
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|shouldGet ()
specifier|private
name|boolean
name|shouldGet
parameter_list|()
block|{
if|if
condition|(
name|ui
operator|.
name|isBatch
argument_list|()
condition|)
block|{
return|return
name|required
return|;
block|}
specifier|final
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"Gerrit Code Review is not shipped with %s\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|neededBy
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"** This library is required by %s. **\n"
argument_list|,
name|neededBy
operator|.
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|required
condition|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"**  This library is required for your configuration. **\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"  If available, Gerrit can take advantage of features\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"  in the library, but will also function without it.\n"
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s and install it now"
argument_list|,
name|download
condition|?
literal|"Download"
else|:
literal|"Copy"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ui
operator|.
name|yesno
argument_list|(
literal|true
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|doGet ()
specifier|private
name|void
name|doGet
parameter_list|()
block|{
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|lib_dir
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|lib_dir
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Die
argument_list|(
literal|"Cannot create "
operator|+
name|lib_dir
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
try|try
block|{
name|remover
operator|.
name|remove
argument_list|(
name|remove
argument_list|)
expr_stmt|;
if|if
condition|(
name|download
condition|)
block|{
name|doGetByHttp
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|doGetByLocalCopy
argument_list|()
expr_stmt|;
block|}
name|verifyFileChecksum
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
try|try
block|{
name|Files
operator|.
name|delete
argument_list|(
name|dst
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Delete failed; leave alone.
block|}
if|if
condition|(
name|ui
operator|.
name|isBatch
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Die
argument_list|(
literal|"error: Cannot get "
operator|+
name|jarUrl
argument_list|,
name|err
argument_list|)
throw|;
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"error: "
operator|+
name|err
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Please download:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|jarUrl
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"and save as:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|dst
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
name|ui
operator|.
name|waitForUser
argument_list|()
expr_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|dst
argument_list|)
condition|)
block|{
name|verifyFileChecksum
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|ui
operator|.
name|yesno
argument_list|(
operator|!
name|required
argument_list|,
literal|"Continue without this library"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Die
argument_list|(
literal|"aborted by user"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|dst
argument_list|)
condition|)
block|{
name|exists
operator|=
literal|true
expr_stmt|;
name|IoUtil
operator|.
name|loadJARs
argument_list|(
name|dst
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doGetByLocalCopy ()
specifier|private
name|void
name|doGetByLocalCopy
parameter_list|()
throws|throws
name|IOException
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|"Copying "
operator|+
name|jarUrl
operator|+
literal|" ..."
argument_list|)
expr_stmt|;
name|Path
name|p
init|=
name|url2file
argument_list|(
name|jarUrl
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"Can not find the %s at this location: %s\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"Please provide alternative URL"
argument_list|)
decl_stmt|;
name|p
operator|=
name|url2file
argument_list|(
name|ui
operator|.
name|readString
argument_list|(
literal|null
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|,
name|name
argument_list|,
name|jarUrl
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Files
operator|.
name|copy
argument_list|(
name|p
argument_list|,
name|dst
argument_list|)
expr_stmt|;
block|}
DECL|method|url2file (String urlString)
specifier|private
specifier|static
name|Path
name|url2file
parameter_list|(
name|String
name|urlString
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|urlString
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|Paths
operator|.
name|get
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
return|return
name|Paths
operator|.
name|get
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|doGetByHttp ()
specifier|private
name|void
name|doGetByHttp
parameter_list|()
throws|throws
name|IOException
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|"Downloading "
operator|+
name|jarUrl
operator|+
literal|" ..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|openHttpStream
argument_list|(
name|jarUrl
argument_list|)
init|;
name|OutputStream
name|out
operator|=
name|Files
operator|.
name|newOutputStream
argument_list|(
name|dst
argument_list|)
init|)
block|{
name|ByteStreams
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|" OK"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|deleteDst
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|" !! FAIL !!"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|err
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
throw|throw
name|err
throw|;
block|}
block|}
DECL|method|openHttpStream (String urlStr)
specifier|private
specifier|static
name|InputStream
name|openHttpStream
parameter_list|(
name|String
name|urlStr
parameter_list|)
throws|throws
name|IOException
block|{
name|ProxySelector
name|proxySelector
init|=
name|ProxySelector
operator|.
name|getDefault
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
decl_stmt|;
name|Proxy
name|proxy
init|=
name|HttpSupport
operator|.
name|proxyFor
argument_list|(
name|proxySelector
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|c
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|(
name|proxy
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|HttpSupport
operator|.
name|response
argument_list|(
name|c
argument_list|)
condition|)
block|{
case|case
name|HttpURLConnection
operator|.
name|HTTP_OK
case|:
return|return
name|c
operator|.
name|getInputStream
argument_list|()
return|;
case|case
name|HttpURLConnection
operator|.
name|HTTP_NOT_FOUND
case|:
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
name|url
operator|.
name|toString
argument_list|()
operator|+
literal|": "
operator|+
name|HttpSupport
operator|.
name|response
argument_list|(
name|c
argument_list|)
operator|+
literal|" "
operator|+
name|c
operator|.
name|getResponseMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
comment|// Use Hashing.sha1 for compatibility.
DECL|method|verifyFileChecksum ()
specifier|private
name|void
name|verifyFileChecksum
parameter_list|()
block|{
if|if
condition|(
name|sha1
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return;
block|}
name|Hasher
name|h
init|=
name|Hashing
operator|.
name|sha1
argument_list|()
operator|.
name|newHasher
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|dst
argument_list|)
init|;
name|OutputStream
name|out
operator|=
name|Funnels
operator|.
name|asOutputStream
argument_list|(
name|h
argument_list|)
init|)
block|{
name|ByteStreams
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|deleteDst
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|Die
argument_list|(
literal|"cannot checksum "
operator|+
name|dst
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|sha1
operator|.
name|equals
argument_list|(
name|h
operator|.
name|hash
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Checksum "
operator|+
name|dst
operator|.
name|getFileName
argument_list|()
operator|+
literal|" OK"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ui
operator|.
name|isBatch
argument_list|()
condition|)
block|{
name|deleteDst
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|Die
argument_list|(
name|dst
operator|+
literal|" SHA-1 checksum does not match"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|ui
operator|.
name|yesno
argument_list|(
literal|null
comment|/* force an answer */
argument_list|,
literal|"error: SHA-1 checksum does not match\nUse %s anyway"
argument_list|,
comment|//
name|dst
operator|.
name|getFileName
argument_list|()
argument_list|)
condition|)
block|{
name|deleteDst
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|Die
argument_list|(
literal|"aborted by user"
argument_list|)
throw|;
block|}
block|}
DECL|method|deleteDst ()
specifier|private
name|void
name|deleteDst
parameter_list|()
block|{
try|try
block|{
name|Files
operator|.
name|delete
argument_list|(
name|dst
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|" Failed to clean up lib: "
operator|+
name|dst
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

