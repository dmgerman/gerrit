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
name|gerrit
operator|.
name|common
operator|.
name|Die
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
name|internal
operator|.
name|storage
operator|.
name|file
operator|.
name|LockFile
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
name|lib
operator|.
name|Constants
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
name|storage
operator|.
name|file
operator|.
name|FileBasedConfig
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
name|FS
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
name|IO
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
name|SystemReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/** Utility functions to help initialize a site. */
end_comment

begin_class
DECL|class|InitUtil
class|class
name|InitUtil
block|{
DECL|method|die (String why)
specifier|static
name|Die
name|die
parameter_list|(
name|String
name|why
parameter_list|)
block|{
return|return
operator|new
name|Die
argument_list|(
name|why
argument_list|)
return|;
block|}
DECL|method|die (String why, Throwable cause)
specifier|static
name|Die
name|die
parameter_list|(
name|String
name|why
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
return|return
operator|new
name|Die
argument_list|(
name|why
argument_list|,
name|cause
argument_list|)
return|;
block|}
DECL|method|savePublic (final FileBasedConfig sec)
specifier|static
name|void
name|savePublic
parameter_list|(
specifier|final
name|FileBasedConfig
name|sec
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|modified
argument_list|(
name|sec
argument_list|)
condition|)
block|{
name|sec
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|saveSecure (final FileBasedConfig sec)
specifier|static
name|void
name|saveSecure
parameter_list|(
specifier|final
name|FileBasedConfig
name|sec
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|modified
argument_list|(
name|sec
argument_list|)
condition|)
block|{
specifier|final
name|byte
index|[]
name|out
init|=
name|Constants
operator|.
name|encode
argument_list|(
name|sec
operator|.
name|toText
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|File
name|path
init|=
name|sec
operator|.
name|getFile
argument_list|()
decl_stmt|;
specifier|final
name|LockFile
name|lf
init|=
operator|new
name|LockFile
argument_list|(
name|path
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|lf
operator|.
name|lock
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot lock "
operator|+
name|path
argument_list|)
throw|;
block|}
try|try
block|{
name|chmod
argument_list|(
literal|0600
argument_list|,
operator|new
name|File
argument_list|(
name|path
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|path
operator|.
name|getName
argument_list|()
operator|+
literal|".lock"
argument_list|)
argument_list|)
expr_stmt|;
name|lf
operator|.
name|write
argument_list|(
name|out
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|lf
operator|.
name|commit
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot commit write to "
operator|+
name|path
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|lf
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|modified (FileBasedConfig cfg)
specifier|private
specifier|static
name|boolean
name|modified
parameter_list|(
name|FileBasedConfig
name|cfg
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|curVers
decl_stmt|;
try|try
block|{
name|curVers
operator|=
name|IO
operator|.
name|readFully
argument_list|(
name|cfg
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|notFound
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
name|byte
index|[]
name|newVers
init|=
name|Constants
operator|.
name|encode
argument_list|(
name|cfg
operator|.
name|toText
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|curVers
argument_list|,
name|newVers
argument_list|)
return|;
block|}
DECL|method|mkdir (final File path)
specifier|static
name|void
name|mkdir
parameter_list|(
specifier|final
name|File
name|path
parameter_list|)
block|{
if|if
condition|(
operator|!
name|path
operator|.
name|isDirectory
argument_list|()
operator|&&
operator|!
name|path
operator|.
name|mkdir
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"Cannot make directory "
operator|+
name|path
argument_list|)
throw|;
block|}
block|}
DECL|method|chmod (final int mode, final File path)
specifier|static
name|void
name|chmod
parameter_list|(
specifier|final
name|int
name|mode
parameter_list|,
specifier|final
name|File
name|path
parameter_list|)
block|{
name|path
operator|.
name|setReadable
argument_list|(
literal|false
argument_list|,
literal|false
comment|/* all */
argument_list|)
expr_stmt|;
name|path
operator|.
name|setWritable
argument_list|(
literal|false
argument_list|,
literal|false
comment|/* all */
argument_list|)
expr_stmt|;
name|path
operator|.
name|setExecutable
argument_list|(
literal|false
argument_list|,
literal|false
comment|/* all */
argument_list|)
expr_stmt|;
name|path
operator|.
name|setReadable
argument_list|(
operator|(
name|mode
operator|&
literal|0400
operator|)
operator|==
literal|0400
argument_list|,
literal|true
comment|/* owner only */
argument_list|)
expr_stmt|;
name|path
operator|.
name|setWritable
argument_list|(
operator|(
name|mode
operator|&
literal|0200
operator|)
operator|==
literal|0200
argument_list|,
literal|true
comment|/* owner only */
argument_list|)
expr_stmt|;
if|if
condition|(
name|path
operator|.
name|isDirectory
argument_list|()
operator|||
operator|(
name|mode
operator|&
literal|0100
operator|)
operator|==
literal|0100
condition|)
block|{
name|path
operator|.
name|setExecutable
argument_list|(
literal|true
argument_list|,
literal|true
comment|/* owner only */
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|mode
operator|&
literal|0044
operator|)
operator|==
literal|0044
condition|)
block|{
name|path
operator|.
name|setReadable
argument_list|(
literal|true
argument_list|,
literal|false
comment|/* all */
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|mode
operator|&
literal|0011
operator|)
operator|==
literal|0011
condition|)
block|{
name|path
operator|.
name|setExecutable
argument_list|(
literal|true
argument_list|,
literal|false
comment|/* all */
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|version ()
specifier|static
name|String
name|version
parameter_list|()
block|{
return|return
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|Version
operator|.
name|getVersion
argument_list|()
return|;
block|}
DECL|method|username ()
specifier|static
name|String
name|username
parameter_list|()
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.name"
argument_list|)
return|;
block|}
DECL|method|hostname ()
specifier|static
name|String
name|hostname
parameter_list|()
block|{
return|return
name|SystemReader
operator|.
name|getInstance
argument_list|()
operator|.
name|getHostname
argument_list|()
return|;
block|}
DECL|method|isLocal (final String hostname)
specifier|static
name|boolean
name|isLocal
parameter_list|(
specifier|final
name|String
name|hostname
parameter_list|)
block|{
try|try
block|{
return|return
name|InetAddress
operator|.
name|getByName
argument_list|(
name|hostname
argument_list|)
operator|.
name|isLoopbackAddress
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|dnOf (String name)
specifier|static
name|String
name|dnOf
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|int
name|p
init|=
name|name
operator|.
name|indexOf
argument_list|(
literal|"://"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|p
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|3
argument_list|)
expr_stmt|;
block|}
name|p
operator|=
name|name
operator|.
name|indexOf
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
if|if
condition|(
literal|0
operator|<
name|p
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|1
argument_list|)
expr_stmt|;
name|name
operator|=
literal|"DC="
operator|+
name|name
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|",DC="
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|name
return|;
block|}
DECL|method|domainOf (String name)
specifier|static
name|String
name|domainOf
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|int
name|p
init|=
name|name
operator|.
name|indexOf
argument_list|(
literal|"://"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|p
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|3
argument_list|)
expr_stmt|;
block|}
name|p
operator|=
name|name
operator|.
name|indexOf
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
if|if
condition|(
literal|0
operator|<
name|p
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|p
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|name
return|;
block|}
DECL|method|extract (final File dst, final Class<?> sibling, final String name)
specifier|static
name|void
name|extract
parameter_list|(
specifier|final
name|File
name|dst
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|sibling
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|in
init|=
name|open
argument_list|(
name|sibling
argument_list|,
name|name
argument_list|)
init|)
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|ByteBuffer
name|buf
init|=
name|IO
operator|.
name|readWholeStream
argument_list|(
name|in
argument_list|,
literal|8192
argument_list|)
decl_stmt|;
name|copy
argument_list|(
name|dst
argument_list|,
name|buf
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|open (final Class<?> sibling, final String name)
specifier|private
specifier|static
name|InputStream
name|open
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|sibling
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
block|{
specifier|final
name|InputStream
name|in
init|=
name|sibling
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
name|String
name|pkg
init|=
name|sibling
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|end
init|=
name|pkg
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|end
condition|)
block|{
name|pkg
operator|=
name|pkg
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|end
operator|+
literal|1
argument_list|)
expr_stmt|;
name|pkg
operator|=
name|pkg
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pkg
operator|=
literal|""
expr_stmt|;
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"warn: Cannot read "
operator|+
name|pkg
operator|+
name|name
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|in
return|;
block|}
DECL|method|copy (final File dst, final ByteBuffer buf)
specifier|static
name|void
name|copy
parameter_list|(
specifier|final
name|File
name|dst
parameter_list|,
specifier|final
name|ByteBuffer
name|buf
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|IOException
block|{
comment|// If the file already has the content we want to put there,
comment|// don't attempt to overwrite the file.
comment|//
try|try
block|{
if|if
condition|(
name|buf
operator|.
name|equals
argument_list|(
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|IO
operator|.
name|readFully
argument_list|(
name|dst
argument_list|)
argument_list|)
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|notFound
parameter_list|)
block|{
comment|// Fall through and write the file.
block|}
name|dst
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|LockFile
name|lf
init|=
operator|new
name|LockFile
argument_list|(
name|dst
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|lf
operator|.
name|lock
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot lock "
operator|+
name|dst
argument_list|)
throw|;
block|}
try|try
block|{
specifier|final
name|OutputStream
name|out
init|=
name|lf
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|byte
index|[]
name|tmp
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
while|while
condition|(
literal|0
operator|<
name|buf
operator|.
name|remaining
argument_list|()
condition|)
block|{
name|int
name|n
init|=
name|Math
operator|.
name|min
argument_list|(
name|buf
operator|.
name|remaining
argument_list|()
argument_list|,
name|tmp
operator|.
name|length
argument_list|)
decl_stmt|;
name|buf
operator|.
name|get
argument_list|(
name|tmp
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|tmp
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|lf
operator|.
name|commit
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot commit "
operator|+
name|dst
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|lf
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|toURI (String url)
specifier|static
name|URI
name|toURI
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|URISyntaxException
block|{
specifier|final
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|url
argument_list|)
decl_stmt|;
if|if
condition|(
name|isAnyAddress
argument_list|(
name|u
argument_list|)
condition|)
block|{
comment|// If the URL uses * it means all addresses on this system, use the
comment|// current hostname instead in the returned URI.
comment|//
specifier|final
name|int
name|s
init|=
name|url
operator|.
name|indexOf
argument_list|(
literal|'*'
argument_list|)
decl_stmt|;
name|url
operator|=
name|url
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
argument_list|)
operator|+
name|hostname
argument_list|()
operator|+
name|url
operator|.
name|substring
argument_list|(
name|s
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|URI
argument_list|(
name|url
argument_list|)
return|;
block|}
DECL|method|isAnyAddress (final URI u)
specifier|static
name|boolean
name|isAnyAddress
parameter_list|(
specifier|final
name|URI
name|u
parameter_list|)
block|{
return|return
name|u
operator|.
name|getHost
argument_list|()
operator|==
literal|null
operator|&&
operator|(
name|u
operator|.
name|getAuthority
argument_list|()
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
operator|||
name|u
operator|.
name|getAuthority
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"*:"
argument_list|)
operator|)
return|;
block|}
DECL|method|portOf (final URI uri)
specifier|static
name|int
name|portOf
parameter_list|(
specifier|final
name|URI
name|uri
parameter_list|)
block|{
name|int
name|port
init|=
name|uri
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|<
literal|0
condition|)
block|{
name|port
operator|=
literal|"https"
operator|.
name|equals
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|?
literal|443
else|:
literal|80
expr_stmt|;
block|}
return|return
name|port
return|;
block|}
DECL|method|InitUtil ()
specifier|private
name|InitUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit

