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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|plugins
operator|.
name|Plugin
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
name|plugins
operator|.
name|StopPluginListener
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|internal
operator|.
name|UniqueAnnotations
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
name|servlet
operator|.
name|ServletModule
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
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
import|;
end_import

begin_comment
comment|/** Filters all HTTP requests passing through the server. */
end_comment

begin_class
DECL|class|AllRequestFilter
specifier|public
specifier|abstract
class|class
name|AllRequestFilter
implements|implements
name|Filter
block|{
DECL|method|module ()
specifier|public
specifier|static
name|ServletModule
name|module
parameter_list|()
block|{
return|return
operator|new
name|ServletModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|DynamicSet
operator|.
name|setOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|AllRequestFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|filter
argument_list|(
literal|"/*"
argument_list|)
operator|.
name|through
argument_list|(
name|FilterProxy
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|StopPluginListener
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|UniqueAnnotations
operator|.
name|create
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
name|FilterProxy
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Singleton
DECL|class|FilterProxy
specifier|static
class|class
name|FilterProxy
implements|implements
name|Filter
implements|,
name|StopPluginListener
block|{
DECL|field|filters
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|AllRequestFilter
argument_list|>
name|filters
decl_stmt|;
DECL|field|initializedFilters
specifier|private
name|DynamicSet
argument_list|<
name|AllRequestFilter
argument_list|>
name|initializedFilters
decl_stmt|;
DECL|field|filterConfig
specifier|private
name|FilterConfig
name|filterConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|FilterProxy (DynamicSet<AllRequestFilter> filters)
name|FilterProxy
parameter_list|(
name|DynamicSet
argument_list|<
name|AllRequestFilter
argument_list|>
name|filters
parameter_list|)
block|{
name|this
operator|.
name|filters
operator|=
name|filters
expr_stmt|;
name|this
operator|.
name|initializedFilters
operator|=
operator|new
name|DynamicSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|filterConfig
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Initializes a filter if needed      *      * @param filter The filter that should get initialized      * @return {@code true} iff filter is now initialized      * @throws ServletException if filter itself fails to init      */
DECL|method|initFilterIfNeeded (AllRequestFilter filter)
specifier|private
specifier|synchronized
name|boolean
name|initFilterIfNeeded
parameter_list|(
name|AllRequestFilter
name|filter
parameter_list|)
throws|throws
name|ServletException
block|{
name|boolean
name|ret
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|filters
operator|.
name|contains
argument_list|(
name|filter
argument_list|)
condition|)
block|{
comment|// Regardless of whether or not the caller checked filter's
comment|// containment in initializedFilters, we better re-check as we're now
comment|// synchronized.
if|if
condition|(
operator|!
name|initializedFilters
operator|.
name|contains
argument_list|(
name|filter
argument_list|)
condition|)
block|{
name|filter
operator|.
name|init
argument_list|(
name|filterConfig
argument_list|)
expr_stmt|;
name|initializedFilters
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ret
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
DECL|method|cleanUpInitializedFilters ()
specifier|private
specifier|synchronized
name|void
name|cleanUpInitializedFilters
parameter_list|()
block|{
name|Iterable
argument_list|<
name|AllRequestFilter
argument_list|>
name|filtersToCleanUp
init|=
name|initializedFilters
decl_stmt|;
name|initializedFilters
operator|=
operator|new
name|DynamicSet
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|AllRequestFilter
name|filter
range|:
name|filtersToCleanUp
control|)
block|{
if|if
condition|(
name|filters
operator|.
name|contains
argument_list|(
name|filter
argument_list|)
condition|)
block|{
name|initializedFilters
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|filter
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest req, ServletResponse res, FilterChain last)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|req
parameter_list|,
name|ServletResponse
name|res
parameter_list|,
name|FilterChain
name|last
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
specifier|final
name|Iterator
argument_list|<
name|AllRequestFilter
argument_list|>
name|itr
init|=
name|filters
operator|.
name|iterator
argument_list|()
decl_stmt|;
operator|new
name|FilterChain
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|req
parameter_list|,
name|ServletResponse
name|res
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|AllRequestFilter
name|filter
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// To avoid {@code synchronized} on the the whole filtering (and
comment|// thereby killing concurrency), we start the below disjunction
comment|// with an unsynchronized check for containment. This
comment|// unsynchronized check is always correct if no filters got
comment|// initialized/cleaned concurrently behind our back.
comment|// The case of concurrently initialized filters is saved by the
comment|// call to initFilterIfNeeded. So that's fine too.
comment|// The case of concurrently cleaned filters between the {@code if}
comment|// condition and the call to {@code doFilter} is not saved by
comment|// anything. If a filter is getting removed concurrently while
comment|// another thread is in those two lines, doFilter might (but need
comment|// not) fail.
comment|//
comment|// Since this failure only occurs if a filter is deleted
comment|// (e.g.: a plugin reloaded) exactly when a thread is in those
comment|// two lines, and it only breaks a single request, we're ok with
comment|// it, given that this is really both really improbable and also
comment|// the "proper" fix for it would basically kill concurrency of
comment|// webrequests.
if|if
condition|(
name|initializedFilters
operator|.
name|contains
argument_list|(
name|filter
argument_list|)
operator|||
name|initFilterIfNeeded
argument_list|(
name|filter
argument_list|)
condition|)
block|{
name|filter
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|last
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (FilterConfig config)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
comment|// Plugins that provide AllRequestFilters might get loaded later at
comment|// runtime, long after this init method had been called. To allow to
comment|// correctly init such plugins' AllRequestFilters, we keep the
comment|// FilterConfig around, and reuse it to lazy init the AllRequestFilters.
name|filterConfig
operator|=
name|config
expr_stmt|;
for|for
control|(
name|AllRequestFilter
name|f
range|:
name|filters
control|)
block|{
name|initFilterIfNeeded
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
specifier|synchronized
name|void
name|destroy
parameter_list|()
block|{
name|Iterable
argument_list|<
name|AllRequestFilter
argument_list|>
name|filtersToDestroy
init|=
name|initializedFilters
decl_stmt|;
name|initializedFilters
operator|=
operator|new
name|DynamicSet
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|AllRequestFilter
name|filter
range|:
name|filtersToDestroy
control|)
block|{
name|filter
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onStopPlugin (Plugin plugin)
specifier|public
name|void
name|onStopPlugin
parameter_list|(
name|Plugin
name|plugin
parameter_list|)
block|{
comment|// In order to allow properly garbage collection, we need to scrub
comment|// initializedFilters clean of filters stemming from plugins as they
comment|// get unloaded.
name|cleanUpInitializedFilters
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|init (FilterConfig config)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{}
block|}
end_class

end_unit

