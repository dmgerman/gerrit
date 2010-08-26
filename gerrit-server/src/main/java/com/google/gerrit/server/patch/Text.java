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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|RawText
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
name|errors
operator|.
name|LargeObjectException
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
name|errors
operator|.
name|MissingObjectException
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
name|AnyObjectId
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
name|ObjectLoader
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
name|ObjectReader
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
name|PersonIdent
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
name|Repository
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
name|revwalk
operator|.
name|RevCommit
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
name|revwalk
operator|.
name|RevWalk
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
name|RawParseUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|universalchardet
operator|.
name|UniversalDetector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|IllegalCharsetNameException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|UnsupportedCharsetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_class
DECL|class|Text
specifier|public
class|class
name|Text
extends|extends
name|RawText
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Text
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ISO_8859_1
specifier|private
specifier|static
specifier|final
name|Charset
name|ISO_8859_1
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
DECL|field|bigFileThreshold
specifier|private
specifier|static
specifier|final
name|int
name|bigFileThreshold
init|=
literal|10
operator|*
literal|1024
operator|*
literal|1024
decl_stmt|;
DECL|field|NO_BYTES
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|NO_BYTES
init|=
block|{}
decl_stmt|;
DECL|field|EMPTY
specifier|public
specifier|static
specifier|final
name|Text
name|EMPTY
init|=
operator|new
name|Text
argument_list|(
name|NO_BYTES
argument_list|)
decl_stmt|;
DECL|method|forCommit (Repository db, ObjectReader reader, AnyObjectId commitId)
specifier|public
specifier|static
name|Text
name|forCommit
parameter_list|(
name|Repository
name|db
parameter_list|,
name|ObjectReader
name|reader
parameter_list|,
name|AnyObjectId
name|commitId
parameter_list|)
throws|throws
name|IOException
block|{
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|RevCommit
name|c
decl_stmt|;
if|if
condition|(
name|commitId
operator|instanceof
name|RevCommit
condition|)
block|{
name|c
operator|=
operator|(
name|RevCommit
operator|)
name|commitId
expr_stmt|;
block|}
else|else
block|{
name|c
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|commitId
argument_list|)
expr_stmt|;
block|}
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|c
operator|.
name|getParentCount
argument_list|()
condition|)
block|{
case|case
literal|0
case|:
break|break;
case|case
literal|1
case|:
block|{
name|RevCommit
name|p
init|=
name|c
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|rw
operator|.
name|parseBody
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"Parent:     "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|reader
operator|.
name|abbreviate
argument_list|(
name|p
argument_list|,
literal|8
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|p
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|")\n"
argument_list|)
expr_stmt|;
break|break;
block|}
default|default:
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|c
operator|.
name|getParentCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|RevCommit
name|p
init|=
name|c
operator|.
name|getParent
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|rw
operator|.
name|parseBody
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|i
operator|==
literal|0
condition|?
literal|"Merge Of:   "
else|:
literal|"            "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|reader
operator|.
name|abbreviate
argument_list|(
name|p
argument_list|,
literal|8
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|p
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|")\n"
argument_list|)
expr_stmt|;
block|}
block|}
name|appendPersonIdent
argument_list|(
name|b
argument_list|,
literal|"Author"
argument_list|,
name|c
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
expr_stmt|;
name|appendPersonIdent
argument_list|(
name|b
argument_list|,
literal|"Commit"
argument_list|,
name|c
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|c
operator|.
name|getFullMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|Text
argument_list|(
name|b
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
return|;
block|}
DECL|method|appendPersonIdent (StringBuilder b, String field, PersonIdent person)
specifier|private
specifier|static
name|void
name|appendPersonIdent
parameter_list|(
name|StringBuilder
name|b
parameter_list|,
name|String
name|field
parameter_list|,
name|PersonIdent
name|person
parameter_list|)
block|{
if|if
condition|(
name|person
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
name|field
operator|+
literal|":    "
argument_list|)
expr_stmt|;
if|if
condition|(
name|person
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|person
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|person
operator|.
name|getEmailAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|person
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd HH:mm:ss ZZZ"
argument_list|)
decl_stmt|;
name|sdf
operator|.
name|setTimeZone
argument_list|(
name|person
operator|.
name|getTimeZone
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|field
operator|+
literal|"Date: "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|sdf
operator|.
name|format
argument_list|(
name|person
operator|.
name|getWhen
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|asString (byte[] content, String encoding)
specifier|public
specifier|static
name|String
name|asString
parameter_list|(
name|byte
index|[]
name|content
parameter_list|,
name|String
name|encoding
parameter_list|)
block|{
return|return
operator|new
name|String
argument_list|(
name|content
argument_list|,
name|charset
argument_list|(
name|content
argument_list|,
name|encoding
argument_list|)
argument_list|)
return|;
block|}
DECL|method|asByteArray (ObjectLoader ldr)
specifier|public
specifier|static
name|byte
index|[]
name|asByteArray
parameter_list|(
name|ObjectLoader
name|ldr
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|LargeObjectException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
name|ldr
operator|.
name|isLarge
argument_list|()
condition|)
block|{
return|return
name|ldr
operator|.
name|getCachedBytes
argument_list|()
return|;
block|}
name|long
name|sz
init|=
name|ldr
operator|.
name|getSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|sz
operator|>
name|bigFileThreshold
operator|||
name|sz
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
throw|throw
operator|new
name|LargeObjectException
argument_list|()
throw|;
name|byte
index|[]
name|buf
decl_stmt|;
try|try
block|{
name|buf
operator|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|sz
index|]
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OutOfMemoryError
name|noMemory
parameter_list|)
block|{
name|LargeObjectException
name|e
decl_stmt|;
name|e
operator|=
operator|new
name|LargeObjectException
argument_list|()
expr_stmt|;
name|e
operator|.
name|initCause
argument_list|(
name|noMemory
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
name|InputStream
name|in
init|=
name|ldr
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|IO
operator|.
name|readFully
argument_list|(
name|in
argument_list|,
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|buf
return|;
block|}
DECL|method|charset (byte[] content, String encoding)
specifier|private
specifier|static
name|Charset
name|charset
parameter_list|(
name|byte
index|[]
name|content
parameter_list|,
name|String
name|encoding
parameter_list|)
block|{
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|UniversalDetector
name|d
init|=
operator|new
name|UniversalDetector
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|d
operator|.
name|handleData
argument_list|(
name|content
argument_list|,
literal|0
argument_list|,
name|content
operator|.
name|length
argument_list|)
expr_stmt|;
name|d
operator|.
name|dataEnd
argument_list|()
expr_stmt|;
name|encoding
operator|=
name|d
operator|.
name|getDetectedCharset
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
return|return
name|ISO_8859_1
return|;
block|}
try|try
block|{
return|return
name|Charset
operator|.
name|forName
argument_list|(
name|encoding
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalCharsetNameException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Invalid detected charset name '"
operator|+
name|encoding
operator|+
literal|"': "
operator|+
name|err
argument_list|)
expr_stmt|;
return|return
name|ISO_8859_1
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedCharsetException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Detected charset '"
operator|+
name|encoding
operator|+
literal|"' not supported: "
operator|+
name|err
argument_list|)
expr_stmt|;
return|return
name|ISO_8859_1
return|;
block|}
block|}
DECL|field|charset
specifier|private
name|Charset
name|charset
decl_stmt|;
DECL|method|Text (final byte[] r)
specifier|public
name|Text
parameter_list|(
specifier|final
name|byte
index|[]
name|r
parameter_list|)
block|{
name|super
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
DECL|method|Text (ObjectLoader ldr)
specifier|public
name|Text
parameter_list|(
name|ObjectLoader
name|ldr
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|LargeObjectException
throws|,
name|IOException
block|{
name|this
argument_list|(
name|asByteArray
argument_list|(
name|ldr
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getContent ()
specifier|public
name|byte
index|[]
name|getContent
parameter_list|()
block|{
return|return
name|content
return|;
block|}
DECL|method|getLine (final int i)
specifier|public
name|String
name|getLine
parameter_list|(
specifier|final
name|int
name|i
parameter_list|)
block|{
return|return
name|getLines
argument_list|(
name|i
argument_list|,
name|i
operator|+
literal|1
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|getLines (final int begin, final int end, boolean dropLF)
specifier|public
name|String
name|getLines
parameter_list|(
specifier|final
name|int
name|begin
parameter_list|,
specifier|final
name|int
name|end
parameter_list|,
name|boolean
name|dropLF
parameter_list|)
block|{
if|if
condition|(
name|begin
operator|==
name|end
condition|)
block|{
return|return
literal|""
return|;
block|}
specifier|final
name|int
name|s
init|=
name|getLineStart
argument_list|(
name|begin
argument_list|)
decl_stmt|;
name|int
name|e
init|=
name|getLineEnd
argument_list|(
name|end
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|dropLF
operator|&&
name|content
index|[
name|e
operator|-
literal|1
index|]
operator|==
literal|'\n'
condition|)
block|{
name|e
operator|--
expr_stmt|;
block|}
return|return
name|decode
argument_list|(
name|s
argument_list|,
name|e
argument_list|)
return|;
block|}
DECL|method|decode (final int s, int e)
specifier|private
name|String
name|decode
parameter_list|(
specifier|final
name|int
name|s
parameter_list|,
name|int
name|e
parameter_list|)
block|{
if|if
condition|(
name|charset
operator|==
literal|null
condition|)
block|{
name|charset
operator|=
name|charset
argument_list|(
name|content
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|RawParseUtils
operator|.
name|decode
argument_list|(
name|charset
argument_list|,
name|content
argument_list|,
name|s
argument_list|,
name|e
argument_list|)
return|;
block|}
DECL|method|getLineStart (final int i)
specifier|private
name|int
name|getLineStart
parameter_list|(
specifier|final
name|int
name|i
parameter_list|)
block|{
return|return
name|lines
operator|.
name|get
argument_list|(
name|i
operator|+
literal|1
argument_list|)
return|;
block|}
DECL|method|getLineEnd (final int i)
specifier|private
name|int
name|getLineEnd
parameter_list|(
specifier|final
name|int
name|i
parameter_list|)
block|{
return|return
name|lines
operator|.
name|get
argument_list|(
name|i
operator|+
literal|2
argument_list|)
return|;
block|}
block|}
end_class

end_unit

