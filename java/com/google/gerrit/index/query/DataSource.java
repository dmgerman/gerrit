begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.index.query
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|query
package|;
end_package

begin_interface
DECL|interface|DataSource
specifier|public
interface|interface
name|DataSource
parameter_list|<
name|T
parameter_list|>
block|{
comment|/** @return an estimate of the number of results from {@link #read()}. */
DECL|method|getCardinality ()
name|int
name|getCardinality
parameter_list|()
function_decl|;
comment|/** @return read from the database and return the results. */
DECL|method|read ()
name|ResultSet
argument_list|<
name|T
argument_list|>
name|read
parameter_list|()
function_decl|;
comment|/** @return read from the database and return the raw results. */
DECL|method|readRaw ()
name|ResultSet
argument_list|<
name|FieldBundle
argument_list|>
name|readRaw
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

