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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
operator|.
name|nullToEmpty
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
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
name|gerrit
operator|.
name|common
operator|.
name|errors
operator|.
name|NoSuchGroupException
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
name|common
operator|.
name|GroupInfo
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
name|MethodNotAllowedException
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
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
name|server
operator|.
name|ReviewDb
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
name|account
operator|.
name|GroupControl
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ListIncludedGroups
specifier|public
class|class
name|ListIncludedGroups
implements|implements
name|RestReadView
argument_list|<
name|GroupResource
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ListIncludedGroups
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|controlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|controlFactory
decl_stmt|;
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|groups
specifier|private
specifier|final
name|Groups
name|groups
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|GroupJson
name|json
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListIncludedGroups ( GroupControl.Factory controlFactory, Provider<ReviewDb> dbProvider, Groups groups, GroupJson json)
name|ListIncludedGroups
parameter_list|(
name|GroupControl
operator|.
name|Factory
name|controlFactory
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|Groups
name|groups
parameter_list|,
name|GroupJson
name|json
parameter_list|)
block|{
name|this
operator|.
name|controlFactory
operator|=
name|controlFactory
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (GroupResource rsrc)
specifier|public
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|apply
parameter_list|(
name|GroupResource
name|rsrc
parameter_list|)
throws|throws
name|MethodNotAllowedException
throws|,
name|OrmException
block|{
if|if
condition|(
name|rsrc
operator|.
name|toAccountGroup
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|()
throw|;
block|}
name|boolean
name|ownerOfParent
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|isOwner
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|included
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|includedGroupUuids
init|=
name|groups
operator|.
name|getIncludes
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|,
name|rsrc
operator|.
name|toAccountGroup
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|includedGroupUuid
range|:
name|includedGroupUuids
control|)
block|{
try|try
block|{
name|GroupControl
name|i
init|=
name|controlFactory
operator|.
name|controlFor
argument_list|(
name|includedGroupUuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|ownerOfParent
operator|||
name|i
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|included
operator|.
name|add
argument_list|(
name|json
operator|.
name|format
argument_list|(
name|i
operator|.
name|getGroup
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|notFound
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Group %s no longer available, included into %s"
argument_list|,
name|includedGroupUuid
argument_list|,
name|rsrc
operator|.
name|getGroup
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|included
argument_list|,
operator|new
name|Comparator
argument_list|<
name|GroupInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|GroupInfo
name|a
parameter_list|,
name|GroupInfo
name|b
parameter_list|)
block|{
name|int
name|cmp
init|=
name|nullToEmpty
argument_list|(
name|a
operator|.
name|name
argument_list|)
operator|.
name|compareTo
argument_list|(
name|nullToEmpty
argument_list|(
name|b
operator|.
name|name
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|!=
literal|0
condition|)
block|{
return|return
name|cmp
return|;
block|}
return|return
name|nullToEmpty
argument_list|(
name|a
operator|.
name|id
argument_list|)
operator|.
name|compareTo
argument_list|(
name|nullToEmpty
argument_list|(
name|b
operator|.
name|id
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|included
return|;
block|}
block|}
end_class

end_unit

