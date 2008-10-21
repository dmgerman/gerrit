begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.codereview.rpc
package|package
name|com
operator|.
name|google
operator|.
name|codereview
operator|.
name|rpc
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|RpcCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|RpcController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_comment
comment|/**  * A simple controller which does not support cancellation.  *<p>  * Users should check {@link #failed()} to determine if a call failed.  */
end_comment

begin_class
DECL|class|SimpleController
specifier|public
class|class
name|SimpleController
implements|implements
name|RpcController
block|{
DECL|field|SLEEP_MIN
specifier|private
specifier|static
specifier|final
name|int
name|SLEEP_MIN
init|=
literal|100
decl_stmt|;
comment|// milliseconds
DECL|field|SLEEP_MAX
specifier|private
specifier|static
specifier|final
name|int
name|SLEEP_MAX
init|=
literal|2
operator|*
literal|60
operator|*
literal|1000
decl_stmt|;
comment|// milliseconds
DECL|field|MAX_ATTEMPT_PERIOD
specifier|private
specifier|static
specifier|final
name|int
name|MAX_ATTEMPT_PERIOD
init|=
literal|30
operator|*
literal|60
operator|*
literal|1000
decl_stmt|;
comment|// milliseconds
DECL|field|SLEEP_RNG
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|Random
argument_list|>
name|SLEEP_RNG
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Random
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Random
name|initialValue
parameter_list|()
block|{
return|return
operator|new
name|Random
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|method|waitTime ()
specifier|private
specifier|static
name|int
name|waitTime
parameter_list|()
block|{
return|return
name|SLEEP_MIN
operator|+
name|SLEEP_RNG
operator|.
name|get
argument_list|()
operator|.
name|nextInt
argument_list|(
name|SLEEP_MAX
operator|-
name|SLEEP_MIN
argument_list|)
return|;
block|}
DECL|field|errorText
specifier|private
name|String
name|errorText
decl_stmt|;
DECL|field|start
specifier|private
name|long
name|start
decl_stmt|;
DECL|method|errorText ()
specifier|public
name|String
name|errorText
parameter_list|()
block|{
return|return
name|errorText
return|;
block|}
DECL|method|failed ()
specifier|public
name|boolean
name|failed
parameter_list|()
block|{
return|return
name|errorText
operator|!=
literal|null
return|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|errorText
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|setFailed (final String reason)
specifier|public
name|void
name|setFailed
parameter_list|(
specifier|final
name|String
name|reason
parameter_list|)
block|{
name|errorText
operator|=
name|reason
expr_stmt|;
block|}
DECL|method|startCancel ()
specifier|public
name|void
name|startCancel
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|isCanceled ()
specifier|public
name|boolean
name|isCanceled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|notifyOnCancel (final RpcCallback<Object> callback)
specifier|public
name|void
name|notifyOnCancel
parameter_list|(
specifier|final
name|RpcCallback
argument_list|<
name|Object
argument_list|>
name|callback
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|retry ()
name|boolean
name|retry
parameter_list|()
block|{
if|if
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
operator|<
name|MAX_ATTEMPT_PERIOD
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|waitTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{
comment|// Just let the thread continue anyway.
block|}
return|return
literal|true
return|;
block|}
if|if
condition|(
name|start
operator|==
literal|0
condition|)
block|{
name|setFailed
argument_list|(
literal|"retry not supported by the RpcChannel"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|final
name|int
name|s
init|=
name|MAX_ATTEMPT_PERIOD
operator|/
literal|1000
decl_stmt|;
name|setFailed
argument_list|(
literal|"cannot complete in<"
operator|+
name|s
operator|+
literal|" seconds"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
DECL|method|markFirstRequest ()
name|void
name|markFirstRequest
parameter_list|()
block|{
name|start
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

