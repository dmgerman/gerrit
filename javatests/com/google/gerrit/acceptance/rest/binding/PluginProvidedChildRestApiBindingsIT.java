begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.binding
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|binding
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
operator|.
name|RevisionResource
operator|.
name|REVISION_KIND
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
name|collect
operator|.
name|ImmutableList
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
name|collect
operator|.
name|ImmutableSet
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|rest
operator|.
name|util
operator|.
name|RestApiCallHelper
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
name|acceptance
operator|.
name|rest
operator|.
name|util
operator|.
name|RestCall
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
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
name|extensions
operator|.
name|restapi
operator|.
name|ChildCollection
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
name|extensions
operator|.
name|restapi
operator|.
name|IdString
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
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
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
name|extensions
operator|.
name|restapi
operator|.
name|RestApiModule
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
name|extensions
operator|.
name|restapi
operator|.
name|RestCollectionModifyView
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
name|extensions
operator|.
name|restapi
operator|.
name|RestModifyView
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
name|extensions
operator|.
name|restapi
operator|.
name|RestReadView
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
name|extensions
operator|.
name|restapi
operator|.
name|RestResource
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
name|extensions
operator|.
name|restapi
operator|.
name|RestView
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
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
name|change
operator|.
name|RevisionResource
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
name|AbstractModule
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
name|TypeLiteral
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests for checking plugin-provided REST API bindings nested under a core collection.  *  *<p>These tests only verify that the plugin-provided REST endpoints are correctly bound, they do  * not test the functionality of the plugin REST endpoints.  */
end_comment

begin_class
DECL|class|PluginProvidedChildRestApiBindingsIT
specifier|public
class|class
name|PluginProvidedChildRestApiBindingsIT
extends|extends
name|AbstractDaemonTest
block|{
comment|/** Resource to bind a child collection. */
DECL|field|TEST_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|TestPluginResource
argument_list|>
argument_list|>
name|TEST_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|TestPluginResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|PLUGIN_NAME
specifier|private
specifier|static
specifier|final
name|String
name|PLUGIN_NAME
init|=
literal|"my-plugin"
decl_stmt|;
DECL|field|TEST_CALLS
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|RestCall
argument_list|>
name|TEST_CALLS
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
comment|// Calls that have the plugin name as part of the collection name
name|RestCall
operator|.
name|get
argument_list|(
literal|"/changes/%s/revisions/%s/"
operator|+
name|PLUGIN_NAME
operator|+
literal|"~test-collection/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/changes/%s/revisions/%s/"
operator|+
name|PLUGIN_NAME
operator|+
literal|"~test-collection/1/detail"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/changes/%s/revisions/%s/"
operator|+
name|PLUGIN_NAME
operator|+
literal|"~test-collection/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/changes/%s/revisions/%s/"
operator|+
name|PLUGIN_NAME
operator|+
literal|"~test-collection/1/update"
argument_list|)
argument_list|,
comment|// Same tests but without the plugin name as part of the collection name. This works as
comment|// long as there is no core collection with the same name (which takes precedence) and no
comment|// other plugin binds a collection with the same name. We highly encourage plugin authors
comment|// to use the fully qualified collection name instead.
name|RestCall
operator|.
name|get
argument_list|(
literal|"/changes/%s/revisions/%s/test-collection/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/changes/%s/revisions/%s/test-collection/1/detail"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/changes/%s/revisions/%s/test-collection/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/changes/%s/revisions/%s/test-collection/1/update"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Module for all sys bindings.    *    *<p>TODO: This should actually just move into MyPluginHttpModule. However, that doesn't work    * currently. This TODO is for fixing this bug.    */
DECL|class|MyPluginSysModule
specifier|static
class|class
name|MyPluginSysModule
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|install
argument_list|(
operator|new
name|RestApiModule
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|TEST_KIND
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"test-collection"
argument_list|)
operator|.
name|to
argument_list|(
name|TestChildCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|postOnCollection
argument_list|(
name|TEST_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|TestPostOnCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|TEST_KIND
argument_list|,
literal|"update"
argument_list|)
operator|.
name|to
argument_list|(
name|TestPost
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|TEST_KIND
argument_list|,
literal|"detail"
argument_list|)
operator|.
name|to
argument_list|(
name|TestGet
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TestPluginResource
specifier|static
class|class
name|TestPluginResource
implements|implements
name|RestResource
block|{}
annotation|@
name|Singleton
DECL|class|TestChildCollection
specifier|static
class|class
name|TestChildCollection
implements|implements
name|ChildCollection
argument_list|<
name|RevisionResource
argument_list|,
name|TestPluginResource
argument_list|>
block|{
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|TestPluginResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
annotation|@
name|Inject
DECL|method|TestChildCollection (DynamicMap<RestView<TestPluginResource>> views)
name|TestChildCollection
parameter_list|(
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|TestPluginResource
argument_list|>
argument_list|>
name|views
parameter_list|)
block|{
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|RestView
argument_list|<
name|RevisionResource
argument_list|>
name|list
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
operator|(
name|RestReadView
argument_list|<
name|RevisionResource
argument_list|>
operator|)
name|resource
lambda|->
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"one"
argument_list|,
literal|"two"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|parse (RevisionResource parent, IdString id)
specifier|public
name|TestPluginResource
name|parse
parameter_list|(
name|RevisionResource
name|parent
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|TestPluginResource
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|TestPluginResource
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|TestPostOnCollection
specifier|static
class|class
name|TestPostOnCollection
implements|implements
name|RestCollectionModifyView
argument_list|<
name|RevisionResource
argument_list|,
name|TestPluginResource
argument_list|,
name|String
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (RevisionResource parentResource, String input)
specifier|public
name|Object
name|apply
parameter_list|(
name|RevisionResource
name|parentResource
parameter_list|,
name|String
name|input
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|"test"
return|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|TestPost
specifier|static
class|class
name|TestPost
implements|implements
name|RestModifyView
argument_list|<
name|TestPluginResource
argument_list|,
name|String
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (TestPluginResource resource, String input)
specifier|public
name|String
name|apply
parameter_list|(
name|TestPluginResource
name|resource
parameter_list|,
name|String
name|input
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|"test"
return|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|TestGet
specifier|static
class|class
name|TestGet
implements|implements
name|RestReadView
argument_list|<
name|TestPluginResource
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (TestPluginResource resource)
specifier|public
name|String
name|apply
parameter_list|(
name|TestPluginResource
name|resource
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|"test"
return|;
block|}
block|}
annotation|@
name|Test
DECL|method|testEndpoints ()
specifier|public
name|void
name|testEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|PatchSet
operator|.
name|Id
name|patchSetId
init|=
name|createChange
argument_list|()
operator|.
name|getPatchSetId
argument_list|()
decl_stmt|;
try|try
init|(
name|AutoCloseable
name|ignored
init|=
name|installPlugin
argument_list|(
name|PLUGIN_NAME
argument_list|,
name|MyPluginSysModule
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
init|)
block|{
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|TEST_CALLS
operator|.
name|asList
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|patchSetId
operator|.
name|changeId
operator|.
name|id
argument_list|)
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|patchSetId
operator|.
name|patchSetId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

