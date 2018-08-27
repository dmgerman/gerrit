begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
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
name|server
operator|.
name|logging
operator|.
name|TraceContext
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
name|util
operator|.
name|RequestId
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
name|PrintWriter
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
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|SshCommand
specifier|public
specifier|abstract
class|class
name|SshCommand
extends|extends
name|BaseCommand
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--trace"
argument_list|,
name|usage
operator|=
literal|"enable request tracing"
argument_list|)
DECL|field|trace
specifier|private
name|boolean
name|trace
decl_stmt|;
DECL|field|stdout
specifier|protected
name|PrintWriter
name|stdout
decl_stmt|;
DECL|field|stderr
specifier|protected
name|PrintWriter
name|stderr
decl_stmt|;
annotation|@
name|Override
DECL|method|start (Environment env)
specifier|public
name|void
name|start
parameter_list|(
name|Environment
name|env
parameter_list|)
throws|throws
name|IOException
block|{
name|startThread
argument_list|(
operator|new
name|CommandRunnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|parseCommandLine
argument_list|()
expr_stmt|;
name|stdout
operator|=
name|toPrintWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|stderr
operator|=
name|toPrintWriter
argument_list|(
name|err
argument_list|)
expr_stmt|;
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|enableTracing
argument_list|()
init|)
block|{
name|SshCommand
operator|.
name|this
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|stdout
operator|.
name|flush
argument_list|()
expr_stmt|;
name|stderr
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|run ()
specifier|protected
specifier|abstract
name|void
name|run
parameter_list|()
throws|throws
name|UnloggedFailure
throws|,
name|Failure
throws|,
name|Exception
function_decl|;
DECL|method|enableTracing ()
specifier|private
name|TraceContext
name|enableTracing
parameter_list|()
block|{
if|if
condition|(
name|trace
condition|)
block|{
name|RequestId
name|traceId
init|=
operator|new
name|RequestId
argument_list|()
decl_stmt|;
name|stderr
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s: %s"
argument_list|,
name|RequestId
operator|.
name|Type
operator|.
name|TRACE_ID
argument_list|,
name|traceId
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|forceLogging
argument_list|()
operator|.
name|addTag
argument_list|(
name|RequestId
operator|.
name|Type
operator|.
name|TRACE_ID
argument_list|,
name|traceId
argument_list|)
return|;
block|}
return|return
name|TraceContext
operator|.
name|DISABLED
return|;
block|}
block|}
end_class

end_unit

