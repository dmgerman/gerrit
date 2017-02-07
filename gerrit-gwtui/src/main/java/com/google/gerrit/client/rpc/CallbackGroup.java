begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Class for grouping together callbacks and calling them in order.  *  *<p>Callbacks are added to the group with {@link #add(AsyncCallback)}, which returns a wrapped  * callback suitable for passing to an asynchronous RPC call. The last callback must be added using  * {@link #addFinal(AsyncCallback)} or {@link #done()} must be invoked.  *  *<p>The enclosing group buffers returned results and ensures that {@code onSuccess} is called  * exactly once for each callback in the group, in the same order that callbacks were added. This  * allows callers to, for example, use a {@link ScreenLoadCallback} as the last callback in the list  * and only display the screen once all callbacks have succeeded.  *  *<p>In the event of a failure, the<em>first</em> caught exception is sent to<em>all</em>  * callbacks' {@code onFailure} methods, in order; subsequent successes or failures are all ignored.  * Note that this means {@code onFailure} may be called with an exception unrelated to the callback  * processing it.  */
end_comment

begin_class
DECL|class|CallbackGroup
specifier|public
class|class
name|CallbackGroup
block|{
DECL|field|callbacks
specifier|private
specifier|final
name|List
argument_list|<
name|CallbackGlue
argument_list|>
name|callbacks
decl_stmt|;
DECL|field|remaining
specifier|private
specifier|final
name|Set
argument_list|<
name|CallbackGlue
argument_list|>
name|remaining
decl_stmt|;
DECL|field|finalAdded
specifier|private
name|boolean
name|finalAdded
decl_stmt|;
DECL|field|failed
specifier|private
name|boolean
name|failed
decl_stmt|;
DECL|field|failedThrowable
specifier|private
name|Throwable
name|failedThrowable
decl_stmt|;
DECL|method|emptyCallback ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Callback
argument_list|<
name|T
argument_list|>
name|emptyCallback
parameter_list|()
block|{
return|return
operator|new
name|Callback
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|T
name|result
parameter_list|)
block|{}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|err
parameter_list|)
block|{}
block|}
return|;
block|}
DECL|method|CallbackGroup ()
specifier|public
name|CallbackGroup
parameter_list|()
block|{
name|callbacks
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|remaining
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|addEmpty ()
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Callback
argument_list|<
name|T
argument_list|>
name|addEmpty
parameter_list|()
block|{
name|Callback
argument_list|<
name|T
argument_list|>
name|cb
init|=
name|emptyCallback
argument_list|()
decl_stmt|;
return|return
name|add
argument_list|(
name|cb
argument_list|)
return|;
block|}
DECL|method|add (final AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Callback
argument_list|<
name|T
argument_list|>
name|add
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|checkFinalAdded
argument_list|()
expr_stmt|;
return|return
name|handleAdd
argument_list|(
name|cb
argument_list|)
return|;
block|}
DECL|method|add (HttpCallback<T> cb)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|HttpCallback
argument_list|<
name|T
argument_list|>
name|add
parameter_list|(
name|HttpCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|checkFinalAdded
argument_list|()
expr_stmt|;
return|return
name|handleAdd
argument_list|(
name|cb
argument_list|)
return|;
block|}
DECL|method|addFinal (final AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Callback
argument_list|<
name|T
argument_list|>
name|addFinal
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|checkFinalAdded
argument_list|()
expr_stmt|;
name|finalAdded
operator|=
literal|true
expr_stmt|;
return|return
name|handleAdd
argument_list|(
name|cb
argument_list|)
return|;
block|}
DECL|method|addFinal (final HttpCallback<T> cb)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|HttpCallback
argument_list|<
name|T
argument_list|>
name|addFinal
parameter_list|(
specifier|final
name|HttpCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|checkFinalAdded
argument_list|()
expr_stmt|;
name|finalAdded
operator|=
literal|true
expr_stmt|;
return|return
name|handleAdd
argument_list|(
name|cb
argument_list|)
return|;
block|}
DECL|method|done ()
specifier|public
name|void
name|done
parameter_list|()
block|{
name|finalAdded
operator|=
literal|true
expr_stmt|;
name|apply
argument_list|()
expr_stmt|;
block|}
DECL|method|addListener (AsyncCallback<Void> cb)
specifier|public
name|void
name|addListener
parameter_list|(
name|AsyncCallback
argument_list|<
name|Void
argument_list|>
name|cb
parameter_list|)
block|{
if|if
condition|(
operator|!
name|failed
operator|&&
name|finalAdded
operator|&&
name|remaining
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|cb
operator|.
name|onSuccess
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|handleAdd
argument_list|(
name|cb
argument_list|)
operator|.
name|onSuccess
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addListener (CallbackGroup group)
specifier|public
name|void
name|addListener
parameter_list|(
name|CallbackGroup
name|group
parameter_list|)
block|{
name|addListener
argument_list|(
name|group
operator|.
expr|<
name|Void
operator|>
name|addEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|success (CallbackGlue cb)
specifier|private
name|void
name|success
parameter_list|(
name|CallbackGlue
name|cb
parameter_list|)
block|{
name|remaining
operator|.
name|remove
argument_list|(
name|cb
argument_list|)
expr_stmt|;
name|apply
argument_list|()
expr_stmt|;
block|}
DECL|method|failure (CallbackGlue w, Throwable caught)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|failure
parameter_list|(
name|CallbackGlue
name|w
parameter_list|,
name|Throwable
name|caught
parameter_list|)
block|{
if|if
condition|(
operator|!
name|failed
condition|)
block|{
name|failed
operator|=
literal|true
expr_stmt|;
name|failedThrowable
operator|=
name|caught
expr_stmt|;
block|}
name|remaining
operator|.
name|remove
argument_list|(
name|w
argument_list|)
expr_stmt|;
name|apply
argument_list|()
expr_stmt|;
block|}
DECL|method|apply ()
specifier|private
name|void
name|apply
parameter_list|()
block|{
if|if
condition|(
name|finalAdded
operator|&&
name|remaining
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|failed
condition|)
block|{
for|for
control|(
name|CallbackGlue
name|cb
range|:
name|callbacks
control|)
block|{
name|cb
operator|.
name|applyFailed
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|CallbackGlue
name|cb
range|:
name|callbacks
control|)
block|{
name|cb
operator|.
name|applySuccess
argument_list|()
expr_stmt|;
block|}
block|}
name|callbacks
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|handleAdd (AsyncCallback<T> cb)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|Callback
argument_list|<
name|T
argument_list|>
name|handleAdd
parameter_list|(
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
if|if
condition|(
name|failed
condition|)
block|{
name|cb
operator|.
name|onFailure
argument_list|(
name|failedThrowable
argument_list|)
expr_stmt|;
return|return
name|emptyCallback
argument_list|()
return|;
block|}
name|CallbackImpl
argument_list|<
name|T
argument_list|>
name|wrapper
init|=
operator|new
name|CallbackImpl
argument_list|<>
argument_list|(
name|cb
argument_list|)
decl_stmt|;
name|callbacks
operator|.
name|add
argument_list|(
name|wrapper
argument_list|)
expr_stmt|;
name|remaining
operator|.
name|add
argument_list|(
name|wrapper
argument_list|)
expr_stmt|;
return|return
name|wrapper
return|;
block|}
DECL|method|handleAdd (HttpCallback<T> cb)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|HttpCallback
argument_list|<
name|T
argument_list|>
name|handleAdd
parameter_list|(
name|HttpCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
if|if
condition|(
name|failed
condition|)
block|{
name|cb
operator|.
name|onFailure
argument_list|(
name|failedThrowable
argument_list|)
expr_stmt|;
return|return
operator|new
name|HttpCallback
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|HttpResponse
argument_list|<
name|T
argument_list|>
name|result
parameter_list|)
block|{}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{}
block|}
return|;
block|}
name|HttpCallbackImpl
argument_list|<
name|T
argument_list|>
name|w
init|=
operator|new
name|HttpCallbackImpl
argument_list|<>
argument_list|(
name|cb
argument_list|)
decl_stmt|;
name|callbacks
operator|.
name|add
argument_list|(
name|w
argument_list|)
expr_stmt|;
name|remaining
operator|.
name|add
argument_list|(
name|w
argument_list|)
expr_stmt|;
return|return
name|w
return|;
block|}
DECL|method|checkFinalAdded ()
specifier|private
name|void
name|checkFinalAdded
parameter_list|()
block|{
if|if
condition|(
name|finalAdded
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"final callback already added"
argument_list|)
throw|;
block|}
block|}
DECL|interface|Callback
specifier|public
interface|interface
name|Callback
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AsyncCallback
argument_list|<
name|T
argument_list|>
extends|,
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|common
operator|.
name|AsyncCallback
argument_list|<
name|T
argument_list|>
block|{}
DECL|interface|CallbackGlue
specifier|private
interface|interface
name|CallbackGlue
block|{
DECL|method|applySuccess ()
name|void
name|applySuccess
parameter_list|()
function_decl|;
DECL|method|applyFailed ()
name|void
name|applyFailed
parameter_list|()
function_decl|;
block|}
DECL|class|CallbackImpl
specifier|private
class|class
name|CallbackImpl
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Callback
argument_list|<
name|T
argument_list|>
implements|,
name|CallbackGlue
block|{
DECL|field|delegate
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
DECL|field|result
name|T
name|result
decl_stmt|;
DECL|method|CallbackImpl (AsyncCallback<T> delegate)
name|CallbackImpl
parameter_list|(
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSuccess (T value)
specifier|public
name|void
name|onSuccess
parameter_list|(
name|T
name|value
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|value
expr_stmt|;
name|success
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFailure (Throwable caught)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|failure
argument_list|(
name|this
argument_list|,
name|caught
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|applySuccess ()
specifier|public
name|void
name|applySuccess
parameter_list|()
block|{
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
init|=
name|delegate
decl_stmt|;
if|if
condition|(
name|cb
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|=
literal|null
expr_stmt|;
name|cb
operator|.
name|onSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|applyFailed ()
specifier|public
name|void
name|applyFailed
parameter_list|()
block|{
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
init|=
name|delegate
decl_stmt|;
if|if
condition|(
name|cb
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|=
literal|null
expr_stmt|;
name|result
operator|=
literal|null
expr_stmt|;
name|cb
operator|.
name|onFailure
argument_list|(
name|failedThrowable
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|HttpCallbackImpl
specifier|private
class|class
name|HttpCallbackImpl
parameter_list|<
name|T
parameter_list|>
implements|implements
name|HttpCallback
argument_list|<
name|T
argument_list|>
implements|,
name|CallbackGlue
block|{
DECL|field|delegate
specifier|private
name|HttpCallback
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
DECL|field|result
specifier|private
name|HttpResponse
argument_list|<
name|T
argument_list|>
name|result
decl_stmt|;
DECL|method|HttpCallbackImpl (HttpCallback<T> delegate)
name|HttpCallbackImpl
parameter_list|(
name|HttpCallback
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSuccess (HttpResponse<T> result)
specifier|public
name|void
name|onSuccess
parameter_list|(
name|HttpResponse
argument_list|<
name|T
argument_list|>
name|result
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
name|success
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFailure (Throwable caught)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|failure
argument_list|(
name|this
argument_list|,
name|caught
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|applySuccess ()
specifier|public
name|void
name|applySuccess
parameter_list|()
block|{
name|HttpCallback
argument_list|<
name|T
argument_list|>
name|cb
init|=
name|delegate
decl_stmt|;
if|if
condition|(
name|cb
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|=
literal|null
expr_stmt|;
name|cb
operator|.
name|onSuccess
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|applyFailed ()
specifier|public
name|void
name|applyFailed
parameter_list|()
block|{
name|HttpCallback
argument_list|<
name|T
argument_list|>
name|cb
init|=
name|delegate
decl_stmt|;
if|if
condition|(
name|cb
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|=
literal|null
expr_stmt|;
name|result
operator|=
literal|null
expr_stmt|;
name|cb
operator|.
name|onFailure
argument_list|(
name|failedThrowable
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

