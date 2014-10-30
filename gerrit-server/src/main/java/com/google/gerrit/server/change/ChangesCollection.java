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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|AcceptsPost
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
name|ResourceNotFoundException
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
name|RestCollection
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
name|extensions
operator|.
name|restapi
operator|.
name|TopLevelResource
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
name|Change
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
name|ChangeUtil
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
name|CurrentUser
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
name|project
operator|.
name|ChangeControl
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
name|project
operator|.
name|NoSuchChangeException
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
name|query
operator|.
name|change
operator|.
name|QueryChanges
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|Provider
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ChangesCollection
specifier|public
class|class
name|ChangesCollection
implements|implements
name|RestCollection
argument_list|<
name|TopLevelResource
argument_list|,
name|ChangeResource
argument_list|>
implements|,
name|AcceptsPost
argument_list|<
name|TopLevelResource
argument_list|>
block|{
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
decl_stmt|;
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
decl_stmt|;
DECL|field|queryFactory
specifier|private
specifier|final
name|Provider
argument_list|<
name|QueryChanges
argument_list|>
name|queryFactory
decl_stmt|;
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ChangeResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|changeUtil
specifier|private
specifier|final
name|ChangeUtil
name|changeUtil
decl_stmt|;
DECL|field|createChange
specifier|private
specifier|final
name|CreateChange
name|createChange
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangesCollection ( Provider<CurrentUser> user, ChangeControl.GenericFactory changeControlFactory, Provider<QueryChanges> queryFactory, DynamicMap<RestView<ChangeResource>> views, ChangeUtil changeUtil, CreateChange createChange)
name|ChangesCollection
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
parameter_list|,
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
parameter_list|,
name|Provider
argument_list|<
name|QueryChanges
argument_list|>
name|queryFactory
parameter_list|,
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ChangeResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|ChangeUtil
name|changeUtil
parameter_list|,
name|CreateChange
name|createChange
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|changeControlFactory
operator|=
name|changeControlFactory
expr_stmt|;
name|this
operator|.
name|queryFactory
operator|=
name|queryFactory
expr_stmt|;
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|changeUtil
operator|=
name|changeUtil
expr_stmt|;
name|this
operator|.
name|createChange
operator|=
name|createChange
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|QueryChanges
name|list
parameter_list|()
block|{
return|return
name|queryFactory
operator|.
name|get
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
name|ChangeResource
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
annotation|@
name|Override
DECL|method|parse (TopLevelResource root, IdString id)
specifier|public
name|ChangeResource
name|parse
parameter_list|(
name|TopLevelResource
name|root
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|OrmException
block|{
name|List
argument_list|<
name|Change
argument_list|>
name|changes
init|=
name|changeUtil
operator|.
name|findChanges
argument_list|(
name|id
operator|.
name|encoded
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|changes
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
name|ChangeControl
name|control
decl_stmt|;
try|try
block|{
name|control
operator|=
name|changeControlFactory
operator|.
name|validateFor
argument_list|(
name|changes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
return|return
operator|new
name|ChangeResource
argument_list|(
name|control
argument_list|)
return|;
block|}
DECL|method|parse (Change.Id id)
specifier|public
name|ChangeResource
name|parse
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|OrmException
block|{
return|return
name|parse
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|IdString
operator|.
name|fromUrl
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|parse (ChangeControl control)
specifier|public
name|ChangeResource
name|parse
parameter_list|(
name|ChangeControl
name|control
parameter_list|)
block|{
return|return
operator|new
name|ChangeResource
argument_list|(
name|control
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|post (TopLevelResource parent)
specifier|public
name|CreateChange
name|post
parameter_list|(
name|TopLevelResource
name|parent
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|createChange
return|;
block|}
block|}
end_class

end_unit

