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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|client
operator|.
name|reviewdb
operator|.
name|Project
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|rpc
operator|.
name|BaseServiceImplementation
import|;
end_import

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
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
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
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
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
name|List
import|;
end_import

begin_class
DECL|class|SuggestServiceImpl
specifier|public
class|class
name|SuggestServiceImpl
extends|extends
name|BaseServiceImplementation
implements|implements
name|SuggestService
block|{
DECL|method|SuggestServiceImpl (final SchemaFactory<ReviewDb> rdf)
specifier|public
name|SuggestServiceImpl
parameter_list|(
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|rdf
parameter_list|)
block|{
name|super
argument_list|(
name|rdf
argument_list|)
expr_stmt|;
block|}
DECL|method|suggestProjectNameKey (final String query, final int limit, final AsyncCallback<List<Project.NameKey>> callback)
specifier|public
name|void
name|suggestProjectNameKey
parameter_list|(
specifier|final
name|String
name|query
parameter_list|,
specifier|final
name|int
name|limit
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|String
name|a
init|=
name|query
decl_stmt|;
specifier|final
name|String
name|b
init|=
name|a
operator|+
literal|"\uffff"
decl_stmt|;
specifier|final
name|int
name|max
init|=
literal|10
decl_stmt|;
specifier|final
name|int
name|n
init|=
name|limit
operator|<=
literal|0
condition|?
name|max
else|:
name|Math
operator|.
name|min
argument_list|(
name|limit
argument_list|,
name|max
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Project
name|p
range|:
name|db
operator|.
name|projects
argument_list|()
operator|.
name|suggestByName
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
name|n
argument_list|)
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|p
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

