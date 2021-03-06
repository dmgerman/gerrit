begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements. See the NOTICE file distributed with this  * work for additional information regarding copyright ownership. The ASF  * licenses this file to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_comment
comment|/*  * NB: This code was primarly ripped out of MINA SSHD.  *  * @author<a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>  */
end_comment

begin_package
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|flogger
operator|.
name|FluentLogger
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
name|AccessPath
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
name|tools
operator|.
name|ToolsCatalog
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
name|sshd
operator|.
name|BaseCommand
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
name|ByteArrayOutputStream
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
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|Environment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|channel
operator|.
name|ChannelSession
import|;
end_import

begin_class
DECL|class|ScpCommand
specifier|final
class|class
name|ScpCommand
extends|extends
name|BaseCommand
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|TYPE_DIR
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_DIR
init|=
literal|"D"
decl_stmt|;
DECL|field|TYPE_FILE
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_FILE
init|=
literal|"C"
decl_stmt|;
DECL|field|opt_r
specifier|private
name|boolean
name|opt_r
decl_stmt|;
DECL|field|opt_t
specifier|private
name|boolean
name|opt_t
decl_stmt|;
DECL|field|opt_f
specifier|private
name|boolean
name|opt_f
decl_stmt|;
DECL|field|root
specifier|private
name|String
name|root
decl_stmt|;
DECL|field|toc
annotation|@
name|Inject
specifier|private
name|ToolsCatalog
name|toc
decl_stmt|;
DECL|field|error
specifier|private
name|IOException
name|error
decl_stmt|;
annotation|@
name|Override
DECL|method|setArguments (String[] args)
specifier|public
name|void
name|setArguments
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|root
operator|=
literal|""
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'-'
condition|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|1
init|;
name|j
operator|<
name|args
index|[
name|i
index|]
operator|.
name|length
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
switch|switch
condition|(
name|args
index|[
name|i
index|]
operator|.
name|charAt
argument_list|(
name|j
argument_list|)
condition|)
block|{
case|case
literal|'f'
case|:
name|opt_f
operator|=
literal|true
expr_stmt|;
break|break;
case|case
literal|'p'
case|:
break|break;
case|case
literal|'r'
case|:
name|opt_r
operator|=
literal|true
expr_stmt|;
break|break;
case|case
literal|'t'
case|:
name|opt_t
operator|=
literal|true
expr_stmt|;
break|break;
case|case
literal|'v'
case|:
break|break;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|i
operator|==
name|args
operator|.
name|length
operator|-
literal|1
condition|)
block|{
name|root
operator|=
name|args
index|[
name|args
operator|.
name|length
operator|-
literal|1
index|]
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|opt_f
operator|&&
operator|!
name|opt_t
condition|)
block|{
name|error
operator|=
operator|new
name|IOException
argument_list|(
literal|"Either -f or -t option should be set"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|start (ChannelSession channel, Environment env)
specifier|public
name|void
name|start
parameter_list|(
name|ChannelSession
name|channel
parameter_list|,
name|Environment
name|env
parameter_list|)
block|{
name|startThread
argument_list|(
name|this
operator|::
name|runImp
argument_list|,
name|AccessPath
operator|.
name|SSH_COMMAND
argument_list|)
expr_stmt|;
block|}
DECL|method|runImp ()
specifier|private
name|void
name|runImp
parameter_list|()
block|{
try|try
block|{
name|readAck
argument_list|()
expr_stmt|;
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
throw|throw
name|error
throw|;
block|}
if|if
condition|(
name|opt_f
condition|)
block|{
if|if
condition|(
name|root
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|root
operator|=
name|root
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|root
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|root
operator|=
name|root
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|root
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|root
operator|.
name|equals
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|root
operator|=
literal|""
expr_stmt|;
block|}
specifier|final
name|ToolsCatalog
operator|.
name|Entry
name|ent
init|=
name|toc
operator|.
name|get
argument_list|(
name|root
argument_list|)
decl_stmt|;
if|if
condition|(
name|ent
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|root
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|ToolsCatalog
operator|.
name|Entry
operator|.
name|Type
operator|.
name|FILE
operator|==
name|ent
operator|.
name|getType
argument_list|()
condition|)
block|{
name|readFile
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ToolsCatalog
operator|.
name|Entry
operator|.
name|Type
operator|.
name|DIR
operator|==
name|ent
operator|.
name|getType
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|opt_r
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|root
operator|+
literal|" not a regular file"
argument_list|)
throw|;
block|}
name|readDir
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|root
operator|+
literal|" not supported"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unsupported mode"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getClass
argument_list|()
operator|==
name|IOException
operator|.
name|class
operator|&&
literal|"Pipe closed"
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
comment|// Ignore a pipe closed error, its the user disconnecting from us
comment|// while we are waiting for them to stalk.
comment|//
return|return;
block|}
try|try
block|{
name|out
operator|.
name|write
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e2
parameter_list|)
block|{
comment|// Ignore
block|}
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Error in scp command"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|readLine ()
specifier|private
name|String
name|readLine
parameter_list|()
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|int
name|c
init|=
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\n'
condition|)
block|{
return|return
name|baos
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"End of stream"
argument_list|)
throw|;
block|}
else|else
block|{
name|baos
operator|.
name|write
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|readFile (ToolsCatalog.Entry ent)
specifier|private
name|void
name|readFile
parameter_list|(
name|ToolsCatalog
operator|.
name|Entry
name|ent
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|data
init|=
name|ent
operator|.
name|getBytes
argument_list|()
decl_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
name|ent
operator|.
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
name|header
argument_list|(
name|ent
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|readAck
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|ack
argument_list|()
expr_stmt|;
name|readAck
argument_list|()
expr_stmt|;
block|}
DECL|method|readDir (ToolsCatalog.Entry dir)
specifier|private
name|void
name|readDir
parameter_list|(
name|ToolsCatalog
operator|.
name|Entry
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
name|header
argument_list|(
name|dir
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|readAck
argument_list|()
expr_stmt|;
for|for
control|(
name|ToolsCatalog
operator|.
name|Entry
name|e
range|:
name|dir
operator|.
name|getChildren
argument_list|()
control|)
block|{
if|if
condition|(
name|ToolsCatalog
operator|.
name|Entry
operator|.
name|Type
operator|.
name|DIR
operator|==
name|e
operator|.
name|getType
argument_list|()
condition|)
block|{
name|readDir
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|readFile
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|write
argument_list|(
literal|"E\n"
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|readAck
argument_list|()
expr_stmt|;
block|}
DECL|method|header (ToolsCatalog.Entry dir, int len)
specifier|private
name|void
name|header
parameter_list|(
name|ToolsCatalog
operator|.
name|Entry
name|dir
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedEncodingException
block|{
specifier|final
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|dir
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|DIR
case|:
name|buf
operator|.
name|append
argument_list|(
name|TYPE_DIR
argument_list|)
expr_stmt|;
break|break;
case|case
name|FILE
case|:
name|buf
operator|.
name|append
argument_list|(
name|TYPE_FILE
argument_list|)
expr_stmt|;
break|break;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"0"
argument_list|)
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toOctalString
argument_list|(
name|dir
operator|.
name|getMode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// perms
name|buf
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|len
argument_list|)
expr_stmt|;
comment|// length
name|buf
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|dir
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
DECL|method|ack ()
specifier|private
name|void
name|ack
parameter_list|()
throws|throws
name|IOException
block|{
name|out
operator|.
name|write
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
DECL|method|readAck ()
specifier|private
name|void
name|readAck
parameter_list|()
throws|throws
name|IOException
block|{
switch|switch
condition|(
name|in
operator|.
name|read
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
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Received warning: %s"
argument_list|,
name|readLine
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
literal|2
case|:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Received nack: "
operator|+
name|readLine
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

