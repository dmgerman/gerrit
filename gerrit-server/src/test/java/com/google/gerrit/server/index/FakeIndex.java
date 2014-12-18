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
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
package|;
end_package

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
name|query
operator|.
name|Predicate
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
name|QueryParseException
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
name|ChangeData
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
name|ChangeDataSource
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
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
import|;
end_import

begin_class
DECL|class|FakeIndex
class|class
name|FakeIndex
implements|implements
name|ChangeIndex
block|{
DECL|field|V1
specifier|static
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|V1
init|=
operator|new
name|Schema
argument_list|<>
argument_list|(
literal|1
argument_list|,
name|ImmutableList
operator|.
expr|<
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
operator|>
name|of
argument_list|(
name|ChangeField
operator|.
name|STATUS
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|V2
specifier|static
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|V2
init|=
operator|new
name|Schema
argument_list|<>
argument_list|(
literal|2
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|ChangeField
operator|.
name|STATUS
argument_list|,
name|ChangeField
operator|.
name|PATH
argument_list|,
name|ChangeField
operator|.
name|UPDATED
argument_list|)
argument_list|)
decl_stmt|;
DECL|class|Source
specifier|private
specifier|static
class|class
name|Source
implements|implements
name|ChangeDataSource
block|{
DECL|field|p
specifier|private
specifier|final
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
decl_stmt|;
DECL|method|Source (Predicate<ChangeData> p)
name|Source
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
block|{
name|this
operator|.
name|p
operator|=
name|p
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCardinality ()
specifier|public
name|int
name|getCardinality
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|hasChange ()
specifier|public
name|boolean
name|hasChange
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|ResultSet
argument_list|<
name|ChangeData
argument_list|>
name|read
parameter_list|()
throws|throws
name|OrmException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|p
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|field|schema
specifier|private
specifier|final
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
decl_stmt|;
DECL|method|FakeIndex (Schema<ChangeData> schema)
name|FakeIndex
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|replace (ChangeData cd)
specifier|public
name|void
name|replace
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|delete (Change.Id id)
specifier|public
name|void
name|delete
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|deleteAll ()
specifier|public
name|void
name|deleteAll
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getSource (Predicate<ChangeData> p, int start, int limit)
specifier|public
name|ChangeDataSource
name|getSource
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|limit
parameter_list|)
throws|throws
name|QueryParseException
block|{
return|return
operator|new
name|FakeIndex
operator|.
name|Source
argument_list|(
name|p
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getSchema ()
specifier|public
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{   }
annotation|@
name|Override
DECL|method|markReady (boolean ready)
specifier|public
name|void
name|markReady
parameter_list|(
name|boolean
name|ready
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

